package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c==0 || r==c) 1
    else pascal(c-1, r-1) + pascal(c, r-1)
  }
  
  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def counter(lst:List[Char], cntd:Int):Boolean = {
      if (lst.isEmpty)
        cntd == 0
      else {
        val c = lst.head
        val formed = if (c == '(') cntd + 1
        else if (c == ')') cntd -1
        else cntd
        if (formed >= 0)
          counter(lst.tail,formed)
        else
          false
      }
    }
    counter(chars,0)
  }
  
  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    def count( m:Int, n:Int ): Int = {
      if (n == 0) 1
      else if (n < 0) 0
      else if (m <=0 && n >= 1) 0
      else
        count(m - 1, n ) + count(m, n - coins(m-1) )
    }
    count(coins.length, money)
  }

}
