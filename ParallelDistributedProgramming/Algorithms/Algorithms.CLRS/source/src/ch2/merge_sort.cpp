#include <random>
#include <iostream>
#include <cstdlib>

void merge(int A[], int p, int q, int r){
	int n1 = q - p;
	int n2 = r - q;
	int L[n1], R[n2];
	
	for(int i=0; i<n1; i++) {
		L[i] = A[ p + i ];
	}
	for(int j=0; j<n2; j++) {
		R[j] = A[ q + j ];
	}
	
	L[n1]=9999; //sentinel
	R[n2]=9999; //sentinel
	int i=0;
	int j=0;
	for(int k=p; k<r; ++k){
		if( L[i] < R[j]){
			A[k] = L[i++];
		} else {
			A[k] = R[j++];
		}
	}
}

void merge_sort(int A[], int p, int r){
	if (p < r-1){
		int q = ( p + r) / 2;

		merge_sort(A, p, q);
		merge_sort(A, q, r);

		merge(A, p , q, r);
	}
}


int main() {

    //STD11 RANDOM LIB
    std::random_device rd;	// only used once to initialise (seed) engine
    std::mt19937 rng(rd()); // random-number engine used (Mersenne-Twister in this case)
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

    //MERGE-SORTED ARRAY
    std::cout << "Sorted array:\n";
    merge_sort(n, 0, size_n);
    for(auto& x : n){
        std::cout << " " << x;
    }
    std::cout << std::endl;

	return 0;
}
