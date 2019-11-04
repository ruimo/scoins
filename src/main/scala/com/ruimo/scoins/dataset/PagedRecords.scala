package com.ruimo.scoins.dataset

case class PagedRecords[+T] (
  currentPage: Int, // zero is the first page.
  pageSize: Int,    // the number of items in one page.
  pageCount: Long,  // the whle count of pages.
  orderBy: OrderBy,
  records: Seq[T]
) {
  val nextPageExists = currentPage + 1 < pageCount
  val prevPageExists = currentPage > 0
  val isEmpty = records.isEmpty
  val offset: Long = currentPage * pageSize

  def map[B](f: (T) => B): PagedRecords[B] = PagedRecords[B](
    currentPage,
    pageSize,
    pageCount,
    orderBy,
    records.map(f)
  )
}
