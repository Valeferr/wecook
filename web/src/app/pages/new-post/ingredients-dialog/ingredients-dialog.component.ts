import { Component, inject } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, FormsModule, ValidationErrors, Validators } from '@angular/forms';
import { MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { RecipeIngredient } from '../../../model/RecipeIngredient.model';
import { IngredientsService } from '../../../services/ingredients.service';
import { Ingredient } from '../../../model/Ingredient.model';
import { map, Observable, startWith } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-ingredients-dialog',
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
  templateUrl: './ingredients-dialog.component.html',
  styleUrl: './ingredients-dialog.component.css'
})
export class IngredientsDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<IngredientsDialogComponent>);
  private readonly ingredientService = inject(IngredientsService);

  protected ingredientToAdd: RecipeIngredient = new RecipeIngredient();

  protected addIngredientForm: FormGroup;

  protected ingredientsOptions: Array<Ingredient>;
  protected filteredIngredients!: Observable<Ingredient[]>;

  protected maeasurementUnits: Array<string> = new Array<string>();
  protected filteredMeasurementUnits!: Observable<string[]>;

  constructor() {
    this.ingredientsOptions = this.ingredientService.getIngredients();

    this.addIngredientForm = new FormGroup({
      ingredient: new FormControl<string | null>(null, [
        Validators.required,
        (control: AbstractControl): ValidationErrors | null => this.ingredientsOptions.some((i) => i.name === control.value) ? null : { valueNotInList: true }
      ]),
      quantity: new FormControl<number | null>(1, [
        Validators.required
      ]),
      measurementUnit: new FormControl<string | null>(null, [
        Validators.required,
        (control: AbstractControl): ValidationErrors | null => this.maeasurementUnits.includes(control.value) ? null : { valueNotInList: true }
      ])
    });

    this.addIngredientForm.valueChanges.subscribe(value => {
      this.updateSelectedIngredient(value);
    });

    this.filteredIngredients = this.addIngredientForm.controls['ingredient'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.ingredientsOptions.filter(i => i.name.toLowerCase().includes(filterValue))
      }),
    );
  }

  private updateSelectedIngredient(value: any) {
    const selectedIngredient = this.ingredientsOptions.find((i) => i.name === value.ingredient);
    
    if (selectedIngredient?.id !== this.ingredientToAdd.ingredientId) {
      this.addIngredientForm.patchValue({ measurementUnit: null }, { emitEvent: false });
    }
    this.ingredientToAdd.ingredientId = selectedIngredient?.id;

    this.ingredientToAdd.quantity = value.quantity;

    this.ingredientToAdd.measurementUnit = value.measurementUnit;
    this.maeasurementUnits = selectedIngredient?.measurementUnits || new Array<string>();

    this.filteredMeasurementUnits = this.addIngredientForm.controls['measurementUnit'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.maeasurementUnits.filter(i => i.toLowerCase().includes(filterValue))
      }),
    );
  }

  protected onCancel() {
    this.dialogRef.close();
  }
}
