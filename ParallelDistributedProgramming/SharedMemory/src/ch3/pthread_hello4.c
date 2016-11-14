#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define ErrExit(A) fprintf(stderr, A); exit(0)
#define NTHREADS 4

int th_ranks[NTHREADS];
pthread_t hello_ids[NTHREADS];

int GetRank() {
    pthread_t my_id;
    int n, my_rank, status;
    my_id = pthread_self(); // who am I?
    n = 0;
    do
    {
        n++;
        status = pthread_equal(my_id, hello_ids[n]);
    } while(status==0 && n < NTHREADS);
    if(status)
        my_rank=n; // OK, return rank
    else
        my_rank = -1; // else, return error
    return my_rank;
}

void *hello_thread(void *arg){
    int rank = GetRank();
    printf ("Hello Thread %d \n", rank);
    return NULL;
}

int main(int argc, char *argv[]) {
    int i, status;

    for(i=0; i<NTHREADS; i++) {
      status = pthread_create(&hello_ids[i], NULL, hello_thread, NULL);
      if(status!=0) { ErrExit("Error Creating Thread"); }
    }
    for(i=0; i<NTHREADS; i++) {
      status = pthread_join (hello_ids[i], NULL);
      if(status!=0) { ErrExit("Error Joining Thread"); }
    }
    printf("Main Thread: joined\n");
    return 0;
}
