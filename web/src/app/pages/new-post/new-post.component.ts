import { Component, inject, OnInit } from '@angular/core';
import { MainFrameComponent } from "../main-frame/main-frame.component";
import { StepComponent } from './step/step.component';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';
import { Step } from '../../model/Step.model';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { firstValueFrom, map, Observable, startWith } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { RecipeIngredient } from '../../model/RecipeIngredient.model';
import { RecipeService } from '../../services/model/recipe.service';
import { ValueSetsService } from '../../services/model/value-sets.service';
import { StepService } from '../../services/model/step.service';
import { RecipeIngredientService } from '../../services/model/recipe-ingredient.service';

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
  // protected readonly ingredientService = inject(IngredientsService);
  protected readonly valueSetsService = inject(ValueSetsService);
  protected readonly recipeService = inject(RecipeService);
  protected readonly stepService = inject(StepService);
  protected readonly recipeIngredientService = inject(RecipeIngredientService);
  // protected readonly postService = inject(Post);

  protected imageSrc: string = 'assets/default_recipe.png';

  protected steps: Array<Step> = new Array<Step>();
  protected ingredients: Array<RecipeIngredient> = new Array<RecipeIngredient>();

  get duration() {
    return this.steps.reduce((acc, s) => acc + (s.duration || 0), 0);
  }

  protected difficultyOptions: Array<string> = new Array<string>();
  protected filteredDifficulties!: Observable<Array<string>>;

  protected categoryOptions: Array<string> = new Array<string>();
  protected filteredCategories!: Observable<Array<string>>;

  private lastUsedIndex: number = 1;

  protected recipeForm: FormGroup;

  constructor() {
    this.recipeForm = new FormGroup({
      title: new FormControl<string | null>(null, [
        Validators.required
      ]),
      description: new FormControl<string | null>(null, [
        Validators.required
      ]),
      difficulty: new FormControl<string | null>(null, [
        Validators.required,
        (control: AbstractControl): ValidationErrors | null => this.difficultyOptions.includes(control.value) ? null : { valueNotInList: true }
      ]),
      category: new FormControl<string | null>(null, [
        Validators.required,
        (control: AbstractControl): ValidationErrors | null => this.categoryOptions.includes(control.value) ? null : { valueNotInList: true }
      ]),
      image: new FormControl<File | null>(null)
    });

    this.ingredients = new Array<RecipeIngredient>();
  }

  private updateRecipe(value: any) {
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

  async ngOnInit() {
    this.difficultyOptions = await firstValueFrom(this.valueSetsService.difficulties);
    this.categoryOptions = await firstValueFrom(this.valueSetsService.foodCategories);

    this.steps.push(new Step(1, this.lastUsedIndex, "", 1, ""));

    this.recipeForm.valueChanges.subscribe(value => {
      this.updateRecipe(value);
    });

    this.filteredDifficulties = this.recipeForm.controls['difficulty'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.difficultyOptions.filter(o => o.toLowerCase().includes(filterValue))
      }),
    );

    this.filteredCategories = this.recipeForm.controls['category'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.categoryOptions.filter(o => o.toLowerCase().includes(filterValue))
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
    
    // TODO Passare bene tutto
    const newStep = new Step(step.stepIndex + 1, ++this.lastUsedIndex, "", 1, "");
    this.steps.splice(index + 1, 0, newStep);

    this.steps.forEach((s, i) => {
      s.stepIndex = i + 1;
    });
  }

  onStepChange(step: Step) {
    const index = this.steps.findIndex(s => s.stepIndex === step.stepIndex);
    this.steps[index] = step;

    const ingredients = new Map<number, RecipeIngredient>();
    this.steps.forEach(s => {
      s.ingredients.forEach(i => {
        if (ingredients.has(i.ingredient.id!)) {
          ingredients.get(i.ingredient.id!)!.quantity! += i.quantity!;
        } else {
          ingredients.set(i.ingredient.id!, i);
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

  onPublish() {
    debugger;
    this.recipeService.post({
      title: this.recipeForm.controls['title'].value,
      description: this.recipeForm.controls['description'].value,
      category: this.recipeForm.controls['category'].value,
      difficulty: this.recipeForm.controls['difficulty'].value,
    }).subscribe((recipe) => {
      this.steps.forEach((s) => {
        this.stepService.post(recipe.id, {
          description: s.description,
          duration: s.duration,
          action: s.action,
          stepIndex: s.stepIndex
        }).subscribe((step) => {
          s.ingredients.forEach((i) => {
            this.recipeIngredientService.post(recipe.id, step.id, {
              quantity: i.quantity,
              measurementUnit: i.measurementUnit,
              ingredientId: i.ingredient.id,
            }).subscribe((recipeIngredient) => console.log(recipeIngredient));
          });
        });
      });
    });
  }
}
