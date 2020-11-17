#include <iostream>
#include <thread>
#include <vector>

using namespace std;

int n;
vector<vector<int>> matrix;
vector<thread> threads;

int determinant(vector<vector<int>> Matrix) {
    int det = 0;
    if (Matrix.size() == 1) {
        return Matrix[0][0];
    } else if (Matrix.size() == 2) {
        det = (Matrix[0][0] * Matrix[1][1] - Matrix[0][1] * Matrix[1][0]);
        return det;
    } else {
        for (int p = 0; p < Matrix[0].size(); p++) {
            vector<vector<int>> TempMatrix;
            for (int i = 1; i < Matrix.size(); i++) {
                vector<int> tempRow;
                for (int j = 0; j < Matrix[i].size(); j++)
                    if (j != p)
                        tempRow.push_back(Matrix[i][j]);
                if (!tempRow.empty())
                    TempMatrix.push_back(tempRow);
            }
            det = det + Matrix[0][p] * pow(-1, p) * determinant(TempMatrix);
        }
        return det;
    }
}

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

int main(int argc, char *argv[]) {
    cout.precision(3);
    cout << "Введите количество столбцов и строк матрицы: ";
    cin >> n;
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

    cout << "Матрица получена." << "\n"
         << "Определитель матрицы = " << d << "." << "\n\n"
         << "Идёт подсчёт миноров." << "\n";

    vector<vector<int>> minors;
    for (int i = 0; i < n; ++i) {
        minors.emplace_back();
        for (int j = 0; j < n; ++j)
            minors[i].push_back(0);
    }
    cout << "Создано " << n * n << " потоков для подсчёта миноров." << "\n";

    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            threads.emplace_back(countMinor, ref(minors), j, i);

    for (auto &thread : threads)
        thread.join();

    cout << "Миноры посчитаны." << "\n\n"
         << "Транспонируем матрицу." << "\n";

    transpose(minors);

    cout << "Матрица транспонирована." << "\n\n"
         << "Обратная матрица успешно построена." << "\n"
         << "Результат:" << "\n";

    for (int i = 0; i < n; ++i) {
        cout << '\n';
        for (int j = 0; j < n; ++j)
            cout << static_cast<double>(minors[i][j]) / d << ' ';
    }

    return 0;
}