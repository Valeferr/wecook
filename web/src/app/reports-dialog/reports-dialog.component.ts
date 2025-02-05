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
  private readonly valueSet = inject(ValueSetsService);
  private readonly reasons = inject(ValueSetsService);

  protected selectedReportReason: string = '';

  protected reportForm: FormGroup;

  protected reasonsOptions: string[] = [];
  protected filteredReasons!: Observable<Array<string>>;

  constructor() {
    this.reportForm = new FormGroup({
      reason: new FormControl<string | null>(null, [
        Validators.required,
        (control: AbstractControl): ValidationErrors | null => this.reasonsOptions.some((i) => i === control.value) ? null : {valueNotList: true}
      ])
    })
  }

  async ngOnInit() {
    this.reasonsOptions = await firstValueFrom(this.valueSet.reasons);

    this.filteredReasons = this.reportForm.controls['report'].valueChanges.pipe(
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
