import { Component, inject } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ValueSetsService } from '../services/model/value-sets.service';
import { firstValueFrom, map, Observable, startWith } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ReportService } from '../services/model/report.service';
import { CommentReport } from '../model/CommentReport';
import { PostReport } from '../model/PostReport.model';

@Component({
  selector: 'app-reports-dialog',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    AsyncPipe,
    MatAutocompleteModule,
    ReactiveFormsModule
  ],
  templateUrl: './reports-dialog.component.html',
  styleUrl: './reports-dialog.component.css'
})
export class ReportsDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<ReportsDialogComponent>);
  private readonly reportService = inject(ReportService);
  private readonly valueSetService = inject(ValueSetsService);

  protected selectedReportReason: string = '';
  
  protected reportForm: FormGroup;
  protected reasonsOptions: Array<string> = new Array<string>;
  protected filteredReasons!: Observable<Array<string>>;

  constructor() {
    this.reportForm = new FormGroup({
      reason: new FormControl<string | null>(null, [
        Validators.required,
        (control: AbstractControl): ValidationErrors | null => this.reasonsOptions.some((i) => i === control.value) ? null : {valueNotList: true}
      ])
    })

    this.reportForm.valueChanges.subscribe(value => {
      this.selectedReportReason = value.reason;
    });
  }

  async ngOnInit() {
    this.reasonsOptions = await firstValueFrom(this.valueSetService.reasons);

    this.filteredReasons = this.reportForm.controls['reason'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.reasonsOptions.filter(i => i.toLowerCase().includes(filterValue));
      })
    )
  }

  protected onCancel() {
    this.dialogRef.close();
  }
}
