package funsets

object Main extends App {

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

  val a  = union(union(union(union(union(union(s1,s2),s3),s4),s5),s6),s10)
  val b  = union(union(union(union(s6,s7),s8),s9),s10)
  val c  = union(a,b)
  val d  = createSet(11,20)

  printSet( a )
  printSet( b )
  printSet( c )
  println("d = ", FunSets.toString( d ))
  println( contains(a, 1) )
  println( contains(b, 2) )
  println( contains(a, 6) )
  println( contains(b, 7) )
  printSet( intersect(a,b) )
  printSet( diff(a,b) )

}
