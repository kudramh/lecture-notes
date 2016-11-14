cmake_minimum_required(VERSION 3.5)
project(algorithms)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g -std=c++11")

set(EXECUTABLE_OUTPUT_PATH "bin/ch2")
add_executable(insert_sort ./src/ch2/insert_sort.cpp)
add_executable(merge_sort  ./src/ch2/merge_sort.cpp)
add_executable(testx  	   ./src/ch2/testx.cpp)
