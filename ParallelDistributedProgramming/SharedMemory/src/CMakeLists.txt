cmake_minimum_required(VERSION 3.5.2)
project(SharedProgramming)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES)
set(EXECUTABLE_OUTPUT_PATH "bin")

add_executable(pthread_hello1    ./ch3/pthread_hello1.c)
add_executable(pthread_hello2    ./ch3/pthread_hello2.c)
add_executable(pthread_hello3    ./ch3/pthread_hello3.c)
add_executable(pthread_hello4    ./ch3/pthread_hello4.c)
add_executable(pthread_retstring ./ch3/pthread_retstring.c)
add_executable(cpp11_thread1     ./ch3/cpp11_thread1.cpp)
add_executable(cpp11_thread2     ./ch3/cpp11_thread2.cpp)
add_executable(cpp11_thread3     ./ch3/cpp11_thread3.cpp)
add_executable(cpp11_thread4     ./ch3/cpp11_thread4.cpp)
add_executable(cpp11_thread5     ./ch3/cpp11_thread5.cpp)
add_executable(cpp11_thread6     ./ch3/cpp11_thread6.cpp)
add_executable(cpp11_thread7     ./ch3/cpp11_thread7.cpp)
add_executable(cpp11_thread8     ./ch3/cpp11_thread8.cpp)

