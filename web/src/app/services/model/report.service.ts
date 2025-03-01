import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { plainToInstance } from 'class-transformer';
import { map, Observable } from 'rxjs';
import { AuthService } from '../auth.service';
import { Report } from '../../model/Report.model';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private readonly URL: string = 'http://localhost:8080/wecook/report';
  
  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() {}

  public post (
    data: {
      itemId: number,
      type: string,
      reason: string
    }
  ): Observable<Report> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<Report>(`${this.URL}`, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Report, response))      
    );
  }

  public get(
    reportId: number,
  ): Observable<Report> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get(`${this.URL}/${reportId}`, { headers: headers }).pipe(
      map((response) => plainToInstance(Report, response))
    );
  }

  public getAll(
  ): Observable<Array<Report>> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<Report>>(`${this.URL}`, { headers: headers }).pipe(
      map((response) => response.map((report) => plainToInstance(Report, report)))
    )
  }

  public patch (
    reportId: number,
  ): Observable<Report> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.patch<Report>(`${this.URL}/${reportId}`, { headers: headers }).pipe(
      map((response) => plainToInstance(Report, response))
    );
  }

  public putPost (
    reportId: number,
    postId: number
  ): Observable<Report> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.put<Report>(`${this.URL}/${reportId}/post`, postId, { headers: headers }).pipe(
      map((response) => plainToInstance(Report, response))
    );
  }

  public putComment (
    reportId: number,
    commentId: number
  ): Observable<Report> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.put<Report>(`${this.URL}/${reportId}/comment`, commentId, { headers: headers }).pipe(
      map((response) => plainToInstance(Report, response))
    );
  }

  public deletePost (
    reportId: number,
  ): Observable<void> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<void>(`${this.URL}/${reportId}/post`, { headers: headers });
  }
}
