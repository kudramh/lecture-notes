package funsets

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  import FunSets._

  object TestSets {
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

  }
  import TestSets._
  test("Print Sets") {
    println(s"a = ${FunSets.toString(a)}")
    println(s"b = ${FunSets.toString(b)}")
    println(s"c = ${FunSets.toString(c)}")
    println(s"d = ${FunSets.toString(d)}")
    println(s"e = ${FunSets.toString(e)}")
  }

  test("Test Union of a and b sets") {
    val abu = union(a, b)
    assert(forall(a, x => contains(abu,x) ),  "Union - not elements of a")
    assert(forall(b, x => contains(abu,x) ),  "Union - not elements of b")
  }

  test("Test Intersection of a and b sets") {
    val abi = intersect(a, b)
    assert(contains(abi,  6),  "Not contains intersected elements")
    assert(contains(abi, 10),  "Not contains intersected elements")
  }

  test("Test Difference of a and b sets") {
    val abd = diff(a, b)
    assert(contains(abd,  6) === false,  "Difference not implemented")
    assert(contains(abd, 10) === false,  "Difference not implemented")
  }

  test("Test Filter on a and d sets") {
    val even = filter(a, x => (x%2)==0)
    val odd  = filter(d, x => (x%2)==1)
    assert(forall(even, x =>(x%2)==0),  "Not evens filtered")
    assert(forall(odd,  x =>(x%2)==1),  "Not odds filtered")
  }

  test("Test ForAll on d set") {
    assert(forall(d, x => x>10 && x<21),  "For-all not implemented")
  }

  test("Test if Exist odd numbers on d set") {
    assert(exists(d, x => x%2==1),  "For-all not implemented")
  }

  test("Test Map, convert d set to even set") {
    val allEvens = map(d, x => if(x%2==1) x+1 else x )
    printSet(allEvens)
    assert(forall(allEvens, x => x%2==0),  "For-all not implemented")
  }

}
