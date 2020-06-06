import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Request} from '../entities/request';

@Injectable({
  providedIn: 'root'
})
export class DispositivoService {
  private getDispositivosUrl = 'http://localhost:8081/dispositivos';
  private getDispositivoUrl = 'http://localhost:8081/dispositivos/';

  constructor(private httpClient: HttpClient) { }

  sendOrder(req: Request) {
    return this.httpClient.patch<Dispositivo[]>(this.getDispositivosUrl, req);
  }

  actualizarDispositivo(id: number) {
    return this.httpClient.patch<Dispositivo>(this.getDispositivoUrl + id, null)
  }
}
