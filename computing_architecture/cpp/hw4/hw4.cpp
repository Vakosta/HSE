#include <iostream>
#include <vector>
#include <omp.h>

using namespace std;

int n;
vector<vector<int>> matrix;

/**
 * Считает определитель переданной матрицы.
 *
 * @param  lMatrix Матрица, для которой требуется посчитать определитель.
 * @return Полученный определитель.
 */
int determinant(vector<vector<int>> lMatrix) {
    int det = 0;
    if (lMatrix.size() == 1) {
        return lMatrix[0][0];
    } else if (lMatrix.size() == 2) {
        det = (lMatrix[0][0] * lMatrix[1][1] - lMatrix[0][1] * lMatrix[1][0]);
        return det;
    } else {
        for (int p = 0; p < lMatrix[0].size(); p++) {
            vector<vector<int>> TempMatrix;
            for (int i = 1; i < lMatrix.size(); i++) {
                vector<int> tempRow;
                for (int j = 0; j < lMatrix[i].size(); j++)
                    if (j != p)
                        tempRow.push_back(lMatrix[i][j]);
                if (!tempRow.empty())
                    TempMatrix.push_back(tempRow);
            }
            det = det + lMatrix[0][p] * pow(-1, p) * determinant(TempMatrix);
        }
        return det;
    }
}

/**
 * Транспонирует матрицу.
 *
 * @param currentMatrix Матрица, которую требуется транспонировать.
 */
void transpose(vector<vector<int>> &currentMatrix) {
    vector<vector<int>> transpose;

    for (int i = 0; i < currentMatrix.size(); ++i) {
        transpose.emplace_back();
        for (int j = 0; j < currentMatrix.size(); ++j)
            transpose[i].push_back(0);
    }

    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            transpose[i][j] = currentMatrix[j][i];

    currentMatrix = transpose;
}

/**
 * Считает минор для элемента по соответствующим координатам.
 *
 * @param minors Матрица миноров, переданная по ссылке, в которую
 *               будут вноситься все изменения.
 * @param x      Координата x элемента.
 * @param y      Координата y элемента.
 */
void countMinor(vector<vector<int>> &minors, int x, int y) {
    vector<vector<int>> result;

    bool wasI = false;
    for (int i = 0; i < matrix.size(); ++i) {
        for (int j = 0; j < matrix[i].size(); ++j) {
            if (i == y)
                wasI = true;
            if (j != x && i != y) {
                if (result.size() <= i - 1)
                    result.emplace_back();
                result[i - (wasI ? 1 : 0)].push_back(matrix[i][j]);
            }
        }
    }

    minors[y][x] = ((y + x) % 2 == 0 ? 1 : -1) * determinant(result);
}

int main() {
    cout.precision(3);

    cout << "Введите количество столбцов и строк матрицы: ";
    cin >> n;
    if (n <= 0) {
        cout << "Повторите запуск и введите корректное значение.";
        return 0;
    }

    cout << "Введите матрицу:" << '\n';

    for (int i = 0; i < n; ++i) {
        matrix.emplace_back();
        for (int j = 0; j < n; ++j) {
            int value;
            cin >> value;

            matrix[i].push_back(value);
        }
    }

    long d = determinant(matrix);
    if (d == 0) {
        cout << "Определитель равен 0, обратной матрицы не существует.";
        return 0;
    }

    cout << "Матрица получена." << "\n"
         << "Определитель матрицы = " << d << "." << "\n\n"
         << "Идёт подсчёт миноров." << "\n";

    vector<vector<int>> minors;
    for (int i = 0; i < n; ++i) {
        minors.emplace_back();
        for (int j = 0; j < n; ++j)
            minors[i].push_back(0);
    }

#pragma omp parallel for collapse(2)
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            countMinor(minors, j, i);

    cout << "Миноры посчитаны." << "\n\n"
         << "Транспонируем матрицу." << "\n";

    transpose(minors);

    cout << "Матрица транспонирована." << "\n\n"
         << "Обратная матрица успешно построена." << "\n"
         << "Результат:" << "\n";

    for (int i = 0; i < n; ++i) {
        cout << '\n';
        for (int j = 0; j < n; ++j)
            cout << static_cast<double>(minors[i][j]) / d << '\t';
    }

    return 0;
}