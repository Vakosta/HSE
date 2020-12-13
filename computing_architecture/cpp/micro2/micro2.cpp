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
    Fork() {}

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
    cout << "Философ №" << philosopher << " взял вилку №" << fork << endl;
    print_mutex.unlock();
}

void printStartEating(int philosopher, int duration) {
    print_mutex.lock();
    cout << "Философ №" << philosopher << " приступил к трапезе на " << duration << " мс" << endl;
    print_mutex.unlock();
}

void printEndEating(int philosopher) {
    print_mutex.lock();
    cout << "Философ №" << philosopher << " закончил трапезу" << endl;
    print_mutex.unlock();
}

void eat(int numberOfPhilosopher) {
    int counter = 0;
    chrono::duration<long long int, milli> milliseconds = chrono::milliseconds(
            2000 + (rand() % static_cast<int>(6000 - 2000 + 1)));
    while (counter < 100) {
        if (numberOfPhilosopher == 4) {
            forks[0].getFork();
            printFork(numberOfPhilosopher, 0);

            forks[numberOfPhilosopher].getFork();
            printFork(numberOfPhilosopher, numberOfPhilosopher);

            printStartEating(numberOfPhilosopher, milliseconds.count());
            this_thread::sleep_for(milliseconds);
            printEndEating(numberOfPhilosopher);

            forks[0].putFork();
            forks[numberOfPhilosopher].putFork();
        } else {
            forks[numberOfPhilosopher].getFork();
            printFork(numberOfPhilosopher, numberOfPhilosopher);

            forks[numberOfPhilosopher + 1].getFork();
            printFork(numberOfPhilosopher, numberOfPhilosopher + 1);

            printStartEating(numberOfPhilosopher, milliseconds.count());
            this_thread::sleep_for(milliseconds);
            printEndEating(numberOfPhilosopher);

            forks[numberOfPhilosopher].putFork();
            forks[numberOfPhilosopher + 1].putFork();
        }
        counter++;
        this_thread::sleep_for(chrono::milliseconds(10));
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