
def pascal(column:Int, row:Int): Int = {
  if (column==0 || column==row)
    1
  else
    pascal(column-1, row-1) + pascal(column, row-1)
}
pascal(0,5)
pascal(1,5)
pascal(2,5)
pascal(3,5)
pascal(4,5)
pascal(5,5)

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
balance("".toList)
balance("()".toList)
balance("(if (zero? x) max (/ 1 x))".toList)
balance("I told him (that it's not (yet) done).\n(But he wasn't listening)".toList)
balance(":-)".toList)
balance("())(".toList)
balance(")(".toList)
balance("((())))))".toList)

def countChange(money: Int, coins: List[Int]): Int = {
  def count( m:Int, n:Int ): Int = {
    // If n is 0 then there is 1 solution (do not include any coin)
    if (n == 0)
      return 1;
    // If n is less than 0 then no solution exists
    if (n < 0)
      return 0;
    // If there are no coins and n is greater than 0, then no solution exist
    if (m <=0 && n >= 1)
      return 0;
    // count is sum of solutions (i) including S[m-1] (ii) excluding S[m-1]
    return count(m - 1, n ) + count(m, n - coins(m-1) )
  }
  count(coins.length, money)
}
countChange(4,List(1,2))
countChange(300,List(5,10,20,50,100,200,500))
countChange(301,List(5,10,20,50,100,200,500))
countChange(300,List(500,5,50,100,20,200,10))
