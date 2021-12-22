package com.ruimo.scoins.dataset

import com.ruimo.scoins.Scoping._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class PaginationSpec extends AnyFlatSpec with should.Matchers {
  "Pagination" should "not needed if page count is one.." in {
    val pr = PagedRecords[Int](
      currentPage = 0,
      pageSize = 10,
      pageCount = 1,
      orderBy = OrderBy("col"),
      records = Seq()
    )

    assert(Pagination.get(pr) === None)
  }

  it should "Threshold - 1 pages." in {
    val threshold = 5
    val pr = PagedRecords[Int](
      currentPage = 0,
      pageSize = 10,
      pageCount = threshold - 1,
      orderBy = OrderBy("col"),
      records = Seq()
    )

    (0 until threshold).foreach { i =>
      assert(Pagination.get(pr.copy(currentPage = i)) === Some(Pagination(false, false, 0, threshold - 1)))
    }
  }

  it should "Threshold pages." in {
    val threshold = 5
    val pr = PagedRecords[Int](
      currentPage = 0,
      pageSize = 10,
      pageCount = threshold,
      orderBy = OrderBy("col"),
      records = Seq()
    )

    (0 until threshold).foreach { i =>
      assert(Pagination.get(pr.copy(currentPage = i)) === Some(Pagination(false, false, 0, threshold)))
    }
  }

  it should "Threshold + 1 pages." in {
    val threshold = 5
    val pr = PagedRecords[Int](
      currentPage = 0,
      pageSize = 10,
      pageCount = threshold + 1,
      orderBy = OrderBy("col"),
      records = Seq()
    )

    // [0] 1 2 3 4 ... 5
    assert(Pagination.get(pr) === Some(Pagination(false, true, 0, threshold)))

    // 0 [1] 2 3 4 ... 5
    assert(Pagination.get(pr.copy(currentPage = 1)) === Some(Pagination(false, true, 0, threshold)))

    // 0 1 [2] 3 4 ... 5
    assert(Pagination.get(pr.copy(currentPage = 2)) === Some(Pagination(false, true, 0, threshold)))

    // 0 .. 1 2 [3] 4 5
    assert(Pagination.get(pr.copy(currentPage = 3)) === Some(Pagination(true, false, 1, threshold)))

    // 0 .. 1 2 3 [4] 5
    assert(Pagination.get(pr.copy(currentPage = 4)) === Some(Pagination(true, false, 1, threshold)))

    // 0 .. 1 2 3 4 [5]
    assert(Pagination.get(pr.copy(currentPage = 5)) === Some(Pagination(true, false, 1, threshold)))
  }

  it should "Threshold + 2 pages." in {
    val threshold = 5
    val pr = PagedRecords[Int](
      currentPage = 0,
      pageSize = 10,
      pageCount = threshold + 2,
      orderBy = OrderBy("col"),
      records = Seq()
    )

    // [0] 1 2 3 4 ... 6
    assert(Pagination.get(pr) === Some(Pagination(false, true, 0, threshold)))

    // 0 [1] 2 3 4 ... 6
    assert(Pagination.get(pr.copy(currentPage = 1)) === Some(Pagination(false, true, 0, threshold)))

    // 0 1 [2] 3 4 ... 6
    assert(Pagination.get(pr.copy(currentPage = 2)) === Some(Pagination(false, true, 0, threshold)))

    // 0 .. 1 2 [3] 4 5 ... 6
    assert(Pagination.get(pr.copy(currentPage = 3)) === Some(Pagination(true, true, 1, threshold)))

    // 0 .. 2 3 [4] 5 6
    assert(Pagination.get(pr.copy(currentPage = 4)) === Some(Pagination(true, false, 2, threshold)))

    // 0 .. 2 3 4 [5] 6
    assert(Pagination.get(pr.copy(currentPage = 5)) === Some(Pagination(true, false, 2, threshold)))

    // 0 .. 2 3 4 5 [6]
    assert(Pagination.get(pr.copy(currentPage = 6)) === Some(Pagination(true, false, 2, threshold)))
  }

  it should "Threshold + 3 pages." in {
    val threshold = 5
    val pr = PagedRecords[Int](
      currentPage = 0,
      pageSize = 10,
      pageCount = threshold + 3,
      orderBy = OrderBy("col"),
      records = Seq()
    )

    // [0] 1 2 3 4 ... 7
    assert(Pagination.get(pr) === Some(Pagination(false, true, 0, threshold)))

    // 0 [1] 2 3 4 ... 7
    assert(Pagination.get(pr.copy(currentPage = 1)) === Some(Pagination(false, true, 0, threshold)))

    // 0 1 [2] 3 4 ... 7
    assert(Pagination.get(pr.copy(currentPage = 2)) === Some(Pagination(false, true, 0, threshold)))

    // 0 .. 1 2 [3] 4 5 ... 7
    assert(Pagination.get(pr.copy(currentPage = 3)) === Some(Pagination(true, true, 1, threshold)))

    // 0 .. 2 3 [4] 5 6 ... 7
    assert(Pagination.get(pr.copy(currentPage = 4)) === Some(Pagination(true, true, 2, threshold)))

    // 0 .. 3 4 [5] 6 7
    assert(Pagination.get(pr.copy(currentPage = 5)) === Some(Pagination(true, false, 3, threshold)))

    // 0 .. 3 4 5 [6] 7
    assert(Pagination.get(pr.copy(currentPage = 6)) === Some(Pagination(true, false, 3, threshold)))

    // 0 .. 3 4 5 6 [7]
    assert(Pagination.get(pr.copy(currentPage = 6)) === Some(Pagination(true, false, 3, threshold)))
  }
}
