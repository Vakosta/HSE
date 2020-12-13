#include <iostream>
#include <thread>
#include <vector>
#include <mutex>

using namespace std;

class Fork;

vector<Fork> forks;

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

void eat(int numberOfPhilosopher) {
    int counter = 0;
    while (counter < 100) {
        if (numberOfPhilosopher == 4) {
            forks[0].getFork();
            cout << "Философ №" << numberOfPhilosopher << " взял вилку №" << 0 << endl;

            forks[numberOfPhilosopher].getFork();
            cout << "Философ №" << numberOfPhilosopher << " взял вилку №" << numberOfPhilosopher << endl;

            cout << numberOfPhilosopher << endl;
            this_thread::sleep_for(chrono::milliseconds(800 + (rand() % static_cast<int>(2000 - 800 + 1))));

            forks[0].putFork();
            forks[numberOfPhilosopher].putFork();
        } else {
            forks[numberOfPhilosopher].getFork();
            cout << "Философ №" << numberOfPhilosopher << " взял вилку №" << numberOfPhilosopher << endl;

            forks[numberOfPhilosopher + 1].getFork();
            cout << "Философ №" << numberOfPhilosopher << " взял вилку №" << numberOfPhilosopher + 1 << endl;

            this_thread::sleep_for(chrono::milliseconds(500));

            forks[numberOfPhilosopher].putFork();
            forks[numberOfPhilosopher + 1].putFork();
        }
        counter++;
        this_thread::sleep_for(chrono::milliseconds(10));
    }
}

int main() {
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