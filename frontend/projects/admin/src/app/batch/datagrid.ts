export interface DatagridFilter {
  property: string;
  value: string;
}

export interface PageRequest {
  sort?: string[];
  filters?: DatagridFilter[];
  page?: number;
  size?: number;
}

export interface Paginated<T> {
  content: T[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  sort: PaginatedSort;
  numberOfElements: number;
  first: boolean;
  number: number;
  empty: boolean;
}

interface Pageable {
  sort: PaginatedSort;
  offset: number;
  pageNumber: number;
  pageSize: number;
  paged: boolean;
  unpaged: boolean;
}

interface PaginatedSort {
  sorted: boolean;
  unsorted: boolean;
  empty: boolean;
}
