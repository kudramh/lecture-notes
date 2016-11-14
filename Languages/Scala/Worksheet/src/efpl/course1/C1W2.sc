
/**
  * 2. Purely Functional Sets.
  */
object FunSets {
  /**
    * We represent a set by its characteristic function, i.e.
    * its `contains` predicate.
    */
  type Set = Int => Boolean

  /**
    * Indicates whether a set contains a given element.
    */
  def contains(s: Set, elem: Int): Boolean = s(elem)

  /**
    * Returns the set of the one given element.
    */
  def singletonSet(elem: Int): Set = (x => x==elem)


  /**
    * Returns the union of the two given sets,
    * the sets of all elements that are in either `s` or `t`.
    */
  def union(s: Set, t: Set): Set = ( x => contains(s,x) || contains(t,x))

  /**
    * Returns the intersection of the two given sets,
    * the set of all elements that are both in `s` and `t`.
    */
  def intersect(s: Set, t: Set): Set = ( x => contains(s,x) && contains(t,x))

  /**
    * Returns the difference of the two given sets,
    * the set of all elements of `s` that are not in `t`.
    */
  def diff(s: Set, t: Set): Set = ( x => contains(s,x) && !contains(t,x) )

  /**
    * Returns the subset of `s` for which `p` holds.
    */
  def filter(s: Set, p: Int => Boolean): Set = ( x => contains(s,x) && p(x) )


  /**
    * The bounds for `forall` and `exists` are +/- 1000.
    */
  val bound = 1000

  /**
    * Returns whether all bounded integers within `s` satisfy `p`.
    */
  def forall(s: Set, p: Int => Boolean): Boolean = {
    def iter(a: Int): Boolean = {
      if (contains(s,a) && !p(a)) false
      else if (a > bound) true
      else iter(a+1)
    }
    iter(-bound)
  }

  /**
    * Returns whether there exists a bounded integer within `s`
    * that satisfies `p`.
    */
  def exists(s: Set, p: Int => Boolean): Boolean = !forall(s, x => !p(x) )

  /**
    * Returns a set transformed by applying `f` to each element of `s`.
    */
  def map(s: Set, f: Int => Int): Set = (x => exists(s, y => x == f(y)))

  /**
    * Displays the contents of a set
    */
  def toString(s: Set): String = {
    val xs = for (i <- -bound to bound if contains(s, i)) yield i
    xs.mkString("{", ",", "}")
  }

  /**
    * Prints the contents of a set on the console.
    */
  def printSet(s: Set) {
    println(toString(s))
  }

  def createSet(a:Int, b:Int):Set ={
    def loop(set:Set, a:Int):Set = {
      if (a > b)
        set
      else
        union(singletonSet(a), loop(set, a + 1))
    }
    loop(singletonSet(a),a+1)
  }

}
import FunSets._

val s1  = singletonSet(1)
val s2  = singletonSet(2)
val s3  = singletonSet(3)
val s4  = singletonSet(4)
val s5  = singletonSet(5)
val s6  = singletonSet(6)
val s7  = singletonSet(7)
val s8  = singletonSet(8)
val s9  = singletonSet(9)
val s10 = singletonSet(10)
val s11 = singletonSet(11)
val s13 = singletonSet(13)
val s17 = singletonSet(17)

val a  = union(union(union(union(union(union(s1,s2),s3),s4),s5),s6),s10)
val b  = union(union(union(union(s6,s7),s8),s9),s10)
val c  = union(a,b)
val d  = createSet(11,20)
val e  = union(union(s11,s13),s17)

s"a = ${FunSets.toString(a)}"
s"b = ${FunSets.toString(b)}"
s"c = ${FunSets.toString(c)}"
s"d = ${FunSets.toString(d)}"
s"1 E ${contains(a, 1)}"
s"2 E b = ${contains(b, 2)}"
s"6 E a = ${contains(a, 6)}"
s"7 E b = ${contains(b, 7)}"
s"a && b = ${FunSets.toString(intersect(a, b))}"
s"a - b = ${FunSets.toString(diff(a,b))}"
s"all x E a is pair = ${forall( a, x => x%2==0 )}"
s"all x E a < 11 = ${forall( a, x => x<11 )}"
s"all x E a < 10 = ${forall( a, x => x<10 )}"
s" x E a < 10 = ${forall( a, x => x<10 )}"
s" x E a | x/2==5 || x/2==1 ${ exists( a, x => x/2==5 || x/2==1 )}"
s" x E e | x==12 ${ exists( e, x => x==12 )}"



