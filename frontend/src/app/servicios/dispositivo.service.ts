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
    const token = JSON.parse(localStorage.getItem('currentUser')).token;
    console.log(req);
    return this.httpClient.post<Dispositivo[]>(this.sendOrderUrl, req, {
      headers: {
        Authorization: token,
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'DELETE, POST, GET, PATCH, OPTIONS',
      }
    });
  }

  getCurrentGadgets(): Observable<Dispositivo[]>{
    console.log('adentro de getcurrentgadgets');
    const token = JSON.parse(localStorage.getItem('currentUser')).token;
    console.log(token);
    return this.httpClient.get<Dispositivo[]>(this.getCurrentUrl, {
      headers: {
        Authorization: token
      }
    });
  }

  // actualizarDispositivo(id: number) {
  //   return this.httpClient.patch<Dispositivo>(this.getDispositivoUrl + id, null)
  // }
}
