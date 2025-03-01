import { Component, inject, input } from '@angular/core';
import { Router } from '@angular/router';

export interface SearchResult {
  picture: string;
  title: string;
  url: string;
  id: number | string;
}

@Component({
  selector: 'app-search-result',
  standalone: true,
  imports: [],
  templateUrl: './search-result.component.html',
  styleUrl: './search-result.component.css'
})
export class SearchResultComponent {
  protected router: Router = inject(Router);
  item = input.required<SearchResult>();

}
