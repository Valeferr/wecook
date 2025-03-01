import { Component, inject, input } from '@angular/core';
import { Comment } from '../../../model/Comment.model';
import { Router } from '@angular/router';
import { ReportService } from '../../../services/model/report.service';
import { MatDialog } from '@angular/material/dialog';
import { ReportsDialogComponent } from '../../../reports-dialog/reports-dialog.component';

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent {
  private readonly reportService = inject(ReportService);
  private readonly dialog = inject(MatDialog);

  public comment = input.required<Comment>();

  protected router: Router = inject(Router);

  protected openReportDialog(): void {
    const dialogRef = this.dialog.open(ReportsDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.reportService.post({
          itemId: this.comment().id,
          type: 'COMMENT',
          reason: result
        }).subscribe((response) => {
          console.log(response);
        });
      }
    });
  }
}
