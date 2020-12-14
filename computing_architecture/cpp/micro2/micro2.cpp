#include <iostream>
#include <thread>
#include <vector>
#include <mutex>

using namespace std;

class Fork;

vector<Fork> forks;
mutex print_mutex;

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

void printFork(int philosopher, int fork) {
    print_mutex.lock();
    cout << "Философ №" << philosopher + 1 << " взял вилку №" << fork + 1 << endl;
    print_mutex.unlock();
}

void printStartEating(int philosopher, int duration) {
    print_mutex.lock();
    cout << "Философ №" << philosopher + 1 << " приступил к трапезе на " << duration << " мс" << endl;
    print_mutex.unlock();
}

void printEndEating(int philosopher) {
    print_mutex.lock();
    cout << "Философ №" << philosopher + 1 << " закончил трапезу" << endl;
    print_mutex.unlock();
}

void eat(int numberOfPhilosopher) {
    int counter = 0;
    while (counter < 100) {
        int index1, index2;
        if (numberOfPhilosopher == 4)
            index1 = 0, index2 = numberOfPhilosopher;
        else
            index1 = numberOfPhilosopher, index2 = numberOfPhilosopher + 1;

        int minDuration = 1000, maxDuration = 5000;
        chrono::duration<long long int, milli> milliseconds = chrono::milliseconds(
                minDuration + (rand() % static_cast<int>(maxDuration - minDuration + 1)));

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

int main() {
    srand(std::chrono::duration_cast<std::chrono::seconds>(
            std::chrono::system_clock::now().time_since_epoch()).count());
    for (int i = 0; i < 5; ++i)
        forks.emplace_back();

    thread philosopher1(eat, 0);
    thread philosopher2(eat, 1);
    thread philosopher3(eat, 2);
    thread philosopher4(eat, 3);
    thread philosopher5(eat, 4);

    philosopher1.join();
    philosopher2.join();
    philosopher3.join();
    philosopher4.join();
    philosopher5.join();

    return 0;
}