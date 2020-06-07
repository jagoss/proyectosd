import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Request} from '../entities/request';
import {Dispositivo} from '../entities/dispositivo';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DispositivoService {
  private getCurrentUrl = 'http://localhost:8081/gadgets/current';
  private sendOrderUrl = 'http://localhost:8081/gadgets/order';

  constructor(private httpClient: HttpClient) { }

  sendOrder(req: Request): Observable<Dispositivo[]> {
    return this.httpClient.patch<Dispositivo[]>(this.sendOrderUrl, req);
  }

  getCurrentGadgets(): Observable<Dispositivo[]>{
    console.log('adentro de getcurrentgadgets');
    return this.httpClient.get<Dispositivo[]>(this.getCurrentUrl);
  }

  // actualizarDispositivo(id: number) {
  //   return this.httpClient.patch<Dispositivo>(this.getDispositivoUrl + id, null)
  // }
}
