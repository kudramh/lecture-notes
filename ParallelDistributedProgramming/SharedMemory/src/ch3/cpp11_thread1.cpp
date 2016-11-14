#include <iostream>
#include <thread>
#include <chrono>

void threadFunc(unsigned mSecs){
   std::chrono::milliseconds workTime(mSecs);
   std::cout << "Worker running. Sleeping for " << mSecs << std::endl;
   std::this_thread::sleep_for(workTime);
   std::cout << "worker: finished" << std::endl;
}

int main(int argc, char *argv[]){
   std::cout << "main: startup" << std::endl;
   std::thread thrd(threadFunc, 3000);  // thread created
   std::cout << "main: waiting for thread " << std::endl;
   thrd.join();
   std::cout << "main: done" << std::endl;
   return 0;
}
