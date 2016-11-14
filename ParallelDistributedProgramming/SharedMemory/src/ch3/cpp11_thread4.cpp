#include <iostream>
#include <thread>

void ThreadFunc(int& N) {
    N += 10;
}

int main(int argc, char* argv[]) {
   int N = 10;
   std::cout << "Initial value of N = " << N << std::endl;

   //std::thread T1(ThreadFunc, N);  // launch thread to update N
   //T1.join();
   //std::cout << "First value of N = " << N << std::endl;

   // correct way, using the std::ref() construct
   std::thread T2(ThreadFunc, std::ref(N));  // launch thread to update N
   T2.join();
   std::cout << "Second value of N = " << N << std::endl;

   return 0;
}



