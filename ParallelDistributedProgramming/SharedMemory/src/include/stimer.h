#ifndef STIMER_H
#define STIMER_H

#include <iostream>
#include <chrono>

class STimer { 
    private:
        std::chrono::steady_clock::time_point start;
        std::chrono::steady_clock::time_point stop;
    public:
        void Start() {
            start = std::chrono::steady_clock::now();
        }
        void Stop() {
             stop = std::chrono::steady_clock::now();
        }
        void Report() {
            std::chrono::steady_clock::duration delta_start = start.time_since_epoch();
            std::chrono::steady_clock::duration delta_stop  = start.time_since_epoch();
            std::chrono::steady_clock::duration delta = delta_stop - delta_start;
            std::chrono::duration<duration, std::ratio<1,1>> dsecs(d);
            std::cout << "\n Wall time is " << dsecs.count() << " seconds" << std::endl;
        }
};

#endif
