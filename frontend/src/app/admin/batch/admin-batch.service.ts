import { Injectable } from '@angular/core';
import { Logger } from '@service/logger.service';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class AdminBatchService {

  constructor(private logger: Logger, private httpClient: HttpClient) {}

}
