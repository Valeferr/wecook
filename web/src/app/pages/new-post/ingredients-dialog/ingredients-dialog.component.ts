import { Component, inject, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, FormsModule, ValidationErrors, Validators } from '@angular/forms';
import { MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MeasurementUnits, RecipeIngredient } from '../../../model/RecipeIngredient.model';
import { Ingredient } from '../../../model/Ingredient.model';
import { firstValueFrom, map, Observable, startWith } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ReactiveFormsModule } from '@angular/forms';
import { IngredientService } from '../../../services/model/ingredient.service';

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
export class IngredientsDialogComponent implements OnInit{
  private readonly dialogRef = inject(MatDialogRef<IngredientsDialogComponent>);
  private readonly ingredientService = inject(IngredientService);
  
  protected selectedRecipeIngredient: Partial<RecipeIngredient> = {};

  protected addIngredientForm!: FormGroup;

  protected ingredientsOptions: Array<Ingredient> = new Array<Ingredient>();
  protected filteredIngredients!: Observable<Array<Ingredient>>;

  protected measurementUnits: Array<string> = new Array<string>();
  protected filteredMeasurementUnits!: Observable<Array<string>>;

  constructor() {
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
        (control: AbstractControl): ValidationErrors | null => this.measurementUnits.includes(control.value) ? null : { valueNotInList: true }
      ])
    });

    this.addIngredientForm.valueChanges.subscribe(value => {
      this.updateSelectedIngredient(value);
    });
  }

  async ngOnInit() {
    this.ingredientsOptions = await firstValueFrom(this.ingredientService.getAll());

    this.filteredIngredients = this.addIngredientForm.controls['ingredient'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.ingredientsOptions.filter(i => i.name.toLowerCase().includes(filterValue))
      }),
    );

    this.filteredMeasurementUnits = this.addIngredientForm.controls['measurementUnit'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.measurementUnits.filter(i => i.toLowerCase().includes(filterValue));
      }),
    );
  }

  private async updateSelectedIngredient(value: any) {
    console.log(value);
    const selectedIngredient = this.ingredientsOptions.find((i) => i.name === value.ingredient);
    if (!selectedIngredient) {
      this.selectedRecipeIngredient = {};
      this.addIngredientForm.patchValue({ measurementUnit: null }, { emitEvent: false });
      return;
    }

    this.selectedRecipeIngredient.ingredient = selectedIngredient;
    this.selectedRecipeIngredient.quantity = value.quantity;
    this.selectedRecipeIngredient.measurementUnit = value.measurementUnit;

    this.measurementUnits = this.selectedRecipeIngredient.ingredient!.measurementUnits;
  }

  protected onCancel() {
    this.dialogRef.close();
  }
}
