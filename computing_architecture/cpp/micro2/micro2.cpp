#include <iostream>
#include <thread>
#include <vector>
#include <mutex>

using namespace std;

class Fork;

vector<thread> philosophers;
vector<Fork> forks;
mutex print_mutex;

/**
 * Класс вилки с использованием уникального mutex для каждого объекта вилки.
 */
class Fork {
private:
    mutable mutex mtx;

public:
    Fork() = default;

    Fork(Fork const &other) {
        unique_lock<mutex> lock_other(other.mtx);
    }

    void getFork() {
        mtx.lock();
    }

    void putFork() {
        mtx.unlock();
    }
};

/**
 * Метод печати информации в консоль о том, что какой-то философ взял вилку.
 * При этом, перед выводом блокируется print_mutex, который используется для того,
 * чтобы в процессе вывода не возникало конфликтов и нечитабельных строк.
 * После вывода print_mutex разблокируется.
 *
 * @param philosopher Номер философа, который взял вилку.
 * @param fork        Номер вилки, которую взял философ.
 */
void printFork(int philosopher, int fork) {
    print_mutex.lock();
    cout << "Философ №" << philosopher + 1 << " взял вилку №" << fork + 1 << endl;
    print_mutex.unlock();
}

/**
 * Метод печати информации в консоль о том, что какой-то философ приступил к трапезе.
 * При этом, перед выводом блокируется print_mutex, который используется для того,
 * чтобы в процессе вывода не возникало конфликтов и нечитабельных строк.
 * После вывода print_mutex разблокируется.
 *
 * @param philosopher Номер философа, который приступил к трапезе.
 * @param duration    Длительность трапезы.
 */
void printStartEating(int philosopher, int duration) {
    print_mutex.lock();
    cout << "Философ №" << philosopher + 1 << " приступил к трапезе на " << duration << " мс" << endl;
    print_mutex.unlock();
}

/**
 * Метод печати информации в консоль о том, что какой-то философ прекратил приём пищи.
 * При этом, перед выводом блокируется print_mutex, который используется для того,
 * чтобы в процессе вывода не возникало конфликтов и нечитабельных строк.
 * После вывода print_mutex разблокируется.
 *
 * @param philosopher Философ, который прекратил приём пищи.
 */
void printEndEating(int philosopher) {
    print_mutex.lock();
    cout << "Философ №" << philosopher + 1 << " закончил трапезу" << endl;
    print_mutex.unlock();
}

/**
 * Метод приёма пищи одним философом.
 *
 * @param numberOfPhilosopher Номер философа.
 * @param maxIterations       Максимальное количество итераций на одного философа.
 * @param randomMin           Минимальная возможная длительность приёма пищи.
 * @param randomMax           Максимальная возможная длительность приёма пищи.
 */
void eat(int numberOfPhilosopher, int maxIterations, int randomMin, int randomMax) {
    int counter = 0;
    while (counter < maxIterations) {
        int index1, index2;
        if (numberOfPhilosopher == 4)
            index1 = 0, index2 = numberOfPhilosopher;
        else
            index1 = numberOfPhilosopher, index2 = numberOfPhilosopher + 1;

        chrono::duration<long long int, milli> milliseconds = chrono::milliseconds(
                randomMin + (rand() % static_cast<int>(randomMax - randomMin + 1)));

        forks[index1].getFork();
        printFork(numberOfPhilosopher, index1);
        forks[index2].getFork();
        printFork(numberOfPhilosopher, index2);

        printStartEating(numberOfPhilosopher, milliseconds.count());
        this_thread::sleep_for(milliseconds);
        printEndEating(numberOfPhilosopher);

        forks[index1].putFork();
        forks[index2].putFork();

        this_thread::sleep_for(chrono::milliseconds(10));
        counter++;
    }
}

int main(int argc, char *argv[]) {
    string maxIterations(argv[1]);
    string randomMin(argv[2]);
    string randomMax(argv[3]);

    srand(chrono::duration_cast<chrono::seconds>(
            chrono::system_clock::now().time_since_epoch()).count());

    for (int i = 0; i < 5; ++i)
        forks.emplace_back();

    for (int i = 0; i < 5; ++i)
        philosophers.emplace_back(eat, i, stoi(maxIterations), stoi(randomMin), stoi(randomMax));

    for (int i = 0; i < 5; ++i)
        philosophers[i].join();

    return 0;
}