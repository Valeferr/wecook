import { Component, inject, input } from '@angular/core';
import { PostDetailsComponent } from "./post-details/post-details.component";
import { Post } from '../model/Post.model';
import { LikeService } from '../services/model/like.service';
import { SavedPostService } from '../services/model/saved-post.service';
import { MatDialog } from '@angular/material/dialog';
import { ReportsDialogComponent } from '../reports-dialog/reports-dialog.component';
import { ReportService } from '../services/model/report.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [PostDetailsComponent],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent {
  public post = input.required<Post>();

  private readonly likeService = inject(LikeService);
  private readonly savedPostService = inject(SavedPostService);
  private readonly dialog = inject(MatDialog);
  private readonly reportService = inject(ReportService);
  protected router: Router = inject(Router);

  public onLike() {
    this.likeService.post(this.post().id).subscribe(() => {
      this.post().liked = true;
      this.post().likes++;
    });
  }

  public onUnlinke() {
    this.likeService.delete(this.post().id).subscribe(() => {
      this.post().liked = false;
      this.post().likes--;
    });
  }

  public onSave() {
    this.savedPostService.post({
      postId: this.post().id
    }).subscribe(() => {
      this.post().saved = true;
    })
  }

  public onUnsave() {
    this.savedPostService.delete(this.post().id).subscribe(() => {
      this.post().saved = false;
    })
  }

  public onShare() {
    if (navigator.clipboard && navigator.clipboard.writeText) {
      navigator.clipboard.writeText(window.origin + '/recipe/'+ this.post().id);
    } else {
      console.error("Clipboard API non supportata");
    }
  }
  
  protected openReportDialog(): void {
    const dialogRef = this.dialog.open(ReportsDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
      if (result !== undefined) {
        this.reportService.post({
          itemId: this.post().id,
          type: 'POST',
          reason: result
        }).subscribe((response) => {
          console.log(response);
        });
      }
    });
  }
}
