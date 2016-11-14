
#include <iostream>

class Vector {

private:
	double *elem;
	int    size_elem;
public:
	Vector(int s): elem{ new double[s] }, size_elem{s} {
		for(int x=0; x!=s; ++x){
			elem[x]=0;
		}
	}
	~Vector(){
		delete[] elem;
	}
	double& operator[](int i){
		return elem[i];
	}
	int size() const {
		return size_elem;
	}
};


int main(){

	Vector *x = new Vector(3);
	Vector *y = new Vector(3);

	delete x;
	delete y;
	return 0;
}