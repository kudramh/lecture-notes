#include <random>
#include <iostream>
#include <cstdlib>

int main() {


    //STD11 RANDOM LIB
    std::random_device rd;     // only used once to initialise (seed) engine
    std::mt19937 rng(rd());    // random-number engine used (Mersenne-Twister in this case)
    std::uniform_int_distribution<int> uni(1,100); // guaranteed unbiased

    //ARRAY INITIALIZATION
    int size_n = 0;
    std::cout << "Number of array elements: ";
    std::cin >> size_n;
    int n[size_n];
    std::cout << "Random Elements: \n";
    for(auto& x : n){
        x = uni(rng);
        std::cout << x << ' ';
    }
    std::cout << std::endl;

    //INSERTION SORT
    for(int j=1; j<size_n; j++){
        int key = n[j];
        int i = j - 1;
        while( i>=0 && n[i] > key ){
            n[i+1] =  n[i];
            i=i-1;
        }
        n[i+1] = key;
    }

    //PRINT N-SORTED
    std::cout << "Sorted Elements: \n";
    for(auto x : n){
        std::cout << x << ' ';
    }
    std::cout << std::endl;
    return 0;
}