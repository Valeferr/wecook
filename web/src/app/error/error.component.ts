import { Component, inject, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-error',
  standalone: true,
  imports: [],
  templateUrl: './error.component.html',
  styleUrl: './error.component.css'
})
export class ErrorComponent implements OnInit {
  @Input({required: true}) statusCode!: string;

  private route = inject(ActivatedRoute);

  private errors: { [key: string]: { title: string; message: string } } = {
    '400': {
      title: 'Bad request',
      message: 'Sorry, we could not process your request'
    },
    '404': {
      title: 'Page not found',
      message: "Sorry, we couldn't find the page you're looking for."
    },
    '500': {
      title: 'Internal server error',
      message: 'Sorry, try again'
    }
  };

  constructor() {}

  ngOnInit(): void {
    this.statusCode = this.route.snapshot?.data['statusCode'] ?? '400';
  }

  public getError(statusCode: string): {title: string, message: string} {
    const error = {
      title: 'Error',
      message: "Sorry, we couldn't help you"
    };

    if (this.errors[statusCode]) {
      error.title = this.errors[statusCode].title;
      error.message = this.errors[statusCode].message;
    }

    return error;
  }
}
