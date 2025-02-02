import { Component, inject, OnInit } from '@angular/core';
import { MainFrameComponent } from "../main-frame/main-frame.component";
import { StepComponent } from './step/step.component';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';
import { Step } from '../../model/Step.model';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Recipe } from '../../model/Recipe.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { map, Observable, startWith } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { RecipeIngredient } from '../../model/RecipeIngredient.model';
import { IngredientsService } from '../../services/ingredients.service';

@Component({
  selector: 'app-new-post',
  standalone: true,
  imports: [
    StepComponent,
    DragDropModule,
    MainFrameComponent,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatInputModule,
    AsyncPipe,
    ReactiveFormsModule
  ],
  templateUrl: './new-post.component.html',
  styleUrl: './new-post.component.css'
})
export class NewPostComponent implements OnInit {
  protected readonly ingredientsService = inject(IngredientsService);

  protected imageSrc: string = 'assets/default_recipe.png';

  protected recipe: Recipe = new Recipe();
  protected steps: Array<Step> = new Array<Step>();
  protected ingredients: Array<RecipeIngredient> = new Array<RecipeIngredient>();

  get duration() {
    return this.steps.reduce((acc, s) => acc + (s.duration || 0), 0);
  }

  protected difficultyOptions: string[] = ['Easy', 'Medium', 'Hard']; // TODO Get from service
  protected filteredOptions!: Observable<string[]>;

  private lastUsedIndex: number = 1;

  protected recipeForm: FormGroup;

  constructor() {
    this.recipeForm = new FormGroup({
      title: new FormControl<string | null>(null),
      difficulty: new FormControl<string | null>(null, [
        Validators.required,
        (control: AbstractControl): ValidationErrors | null => this.difficultyOptions.includes(control.value) ? null : { valueNotInList: true }
      ]),
      description: new FormControl<string | null>(null),
      image: new FormControl<File | null>(null)
    });

    this.ingredients = new Array<RecipeIngredient>();
  }

  private updateRecipe(value: any) {
    this.recipe.title = value.title;
    this.recipe.description = value.description
    this.recipe.difficulty = value.difficulty;

    if (value.image) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imageSrc = reader.result as string;
      };
      reader.readAsDataURL(value.image);
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
  
      this.recipeForm.patchValue({ image: file });
  
      const reader = new FileReader();
      reader.onload = () => {
        this.imageSrc = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  ngOnInit() {
    this.steps.push(new Step(1, this.lastUsedIndex, undefined, 1));

    this.recipeForm.valueChanges.subscribe(value => {
      this.updateRecipe(value);
    });

    this.filteredOptions = this.recipeForm.controls['difficulty'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.difficultyOptions.filter(o => o.toLowerCase().includes(filterValue))
      }),
    );
  }

  drop(event: CdkDragDrop<any[]>) {
    moveItemInArray(this.steps, event.previousIndex, event.currentIndex);

    this.steps.forEach((s, i) => {
      s.stepIndex = i + 1;
    });
  }

  onNewStep(step: Step) {
    const index = this.steps.findIndex(s => s.stepIndex === step.stepIndex);

    const newStep = new Step(step.stepIndex + 1, ++this.lastUsedIndex, undefined, 1);
    this.steps.splice(index + 1, 0, newStep);

    this.steps.forEach((s, i) => {
      s.stepIndex = i + 1;
    });
  }

  onStepChange(step: Step) {
    debugger;
    const index = this.steps.findIndex(s => s.stepIndex === step.stepIndex);
    this.steps[index] = step;

    const ingredients = new Map<number, RecipeIngredient>();
    this.steps.forEach(s => {
      s.ingredients.forEach(i => {
        if (ingredients.has(i.ingredientId!)) {
          ingredients.get(i.ingredientId!)!.quantity! += i.quantity!;
        } else {
          ingredients.set(i.ingredientId!, i);
        }
      });
    });

    this.ingredients = Array.from(ingredients.values());
  }

  onDeleteStep(index: number) {
    if (this.steps.length === 1) {
      return;
    }

    this.steps = this.steps.filter((_, i) => i !== index);

    this.steps.forEach((s, i) => {
      s.stepIndex = i + 1;
    });
  }
}
