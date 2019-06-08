export interface DatatableSort {
  column: DatatableSortColumn;
  prevValue: string;
  newValue: string;
  sorts: DatatableSorts[];
}

interface DatatableSortColumn {
  name: string;
  prop: string;
}

export interface DatatableSorts {
  dir: string;
  prop: string;
}
