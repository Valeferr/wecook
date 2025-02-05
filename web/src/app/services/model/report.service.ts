import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { plainToInstance } from 'class-transformer';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private readonly URL: string = 'http://localhost:8080/wecook/report';
  
  private http = inject(HttpClient);

  constructor() { }

  public post (
    data: {
      standardUserId: number,
      type: string,
      reason: string
    }
  ): Observable<Report> {
    return this.http.post<Report>(`${this.URL}`, data, { withCredentials: true }).pipe(
      map((response) => plainToInstance(Report, response))      
    );
  }

  public getOne (
    reportId: number,
  ): Observable<Report> {
     return this.http.get<Report>(`${this.URL}/${reportId}`, { withCredentials: true }).pipe(
        map((response) => plainToInstance(Report, response))
      );
  }

  public getAll (
  ): Observable<Array<Report>> {
    return this.http.get<Array<Report>>(`${this.URL}`, { withCredentials: true }).pipe(
      map((response) => response.map((report) => plainToInstance(Report, report)))
    )
  }

  public patch (
    reportId: number,
  ): Observable<Report> {
     return this.http.patch<Report>(`${this.URL}/${reportId}`, { withCredentials: true }).pipe(
        map((response) => plainToInstance(Report, response))
      );
  }

  public putPost (
    reportId: number,
    postId: number
  ): Observable<Report> {
     return this.http.put<Report>(`${this.URL}/${reportId}/post`, postId, { withCredentials: true }).pipe(
        map((response) => plainToInstance(Report, response))
      );
  }

  public putComment (
    reportId: number,
    commentId: number
  ): Observable<Report> {
     return this.http.put<Report>(`${this.URL}/${reportId}/comment`, commentId, { withCredentials: true }).pipe(
        map((response) => plainToInstance(Report, response))
      );
  }

  public deletePost (
    reportId: number,
  ): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${reportId}/post`, { withCredentials: true });
  }
}
