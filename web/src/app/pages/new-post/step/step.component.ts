import { Component, inject, input, OnInit, output } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Actions, Step } from '../../../model/Step.model';
import { MatDialog } from '@angular/material/dialog';
import { IngredientsDialogComponent } from '../ingredients-dialog/ingredients-dialog.component';
import { RecipeIngredient } from '../../../model/RecipeIngredient.model';
import { IngredientsService } from '../../../services/ingredients.service';

@Component({
  selector: 'app-step',
  standalone: true,
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    AsyncPipe
  ],
  templateUrl: './step.component.html',
  styleUrl: './step.component.css'
})
export class StepComponent implements OnInit {
  public step = input.required<Step>();
  public onNewStep = output<Step>();
  public onDeleteStep = output<Step>();
  public onStepChange = output<Step>();

  private readonly dialog = inject(MatDialog);
  protected readonly ingredientsService = inject(IngredientsService);

  protected stepForm: FormGroup;

  protected actionOptions: Actions[] = Object.values(Actions); // TODO Get from service
  protected filteredOptions!: Observable<string[]>;

  constructor() {
    this.stepForm = new FormGroup({
      action: new FormControl<string | null>(null, [
        Validators.required,
        (control: AbstractControl): ValidationErrors | null => this.actionOptions.includes(control.value) ? null : { valueNotInList: true }
      ]),
      description: new FormControl<string | null>(null),
      duration: new FormControl<number>(1,  [
        Validators.required
      ])
    });
  }

  ngOnInit() {
    this.stepForm.patchValue({
      action: this.step().action || null,
      description: this.step().description || null,
      duration: this.step().duration || null
    });
    
    this.stepForm.valueChanges.subscribe(value => {
      this.updateStep(value);
    });

    this.filteredOptions = this.stepForm.controls['action'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.actionOptions.filter(o => o.toLowerCase().includes(filterValue))
      }),
    );
  }

  private updateStep(value: any) {
    const updatedStep = new Step(
      this.step().stepIndex,
      this.step().id,
      value.description || null,
      value.duration || null,
      value.action || null,
      this.step().ingredients
    );

    this.onStepChange.emit(updatedStep);
  }
  
  public newStep() {
      this.onNewStep.emit(this.step());
    }

  public deleteStep() {
    this.onDeleteStep.emit(this.step());
  }

  public isValid(): boolean {
    return this.stepForm.valid;
  }

  private addIngredient(ingredient: RecipeIngredient): void {
    const alreadyExistsIngredient = this.step().ingredients.find(i => i.ingredientId === ingredient.ingredientId);
    if (!alreadyExistsIngredient) {
      this.step().ingredients.push(ingredient);
    } else {
      alreadyExistsIngredient.quantity! += ingredient.quantity!;
    }
  }

  protected openIngredientsDialog(): void {
    const dialogRef = this.dialog.open(IngredientsDialogComponent);
    
    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        console.log("new ingredient", result);
        this.addIngredient(result);
        this.onStepChange.emit(this.step());
      }
    });
  }
}
