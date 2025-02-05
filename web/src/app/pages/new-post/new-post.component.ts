import { Component, inject, OnInit, QueryList, ViewChildren } from '@angular/core';
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
import { PostService } from '../../services/model/post.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

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
  protected readonly valueSetsService = inject(ValueSetsService);
  protected readonly recipeService = inject(RecipeService);
  protected readonly stepService = inject(StepService);
  protected readonly recipeIngredientService = inject(RecipeIngredientService);
  protected readonly postService = inject(PostService);
  protected readonly authService = inject(AuthService);
  protected readonly router = inject(Router);

  protected imageSrc: string = 'assets/blank_recipe.png';

  protected steps: Array<Step> = new Array<Step>();
  protected ingredients: Array<RecipeIngredient> = new Array<RecipeIngredient>();

  @ViewChildren(StepComponent) stepComponents!: QueryList<StepComponent>;

  get duration() {
    return this.steps.reduce((acc, s) => acc + (s.duration || 0), 0);
  }

  get areStepsValid() {
    return this.stepComponents ? this.stepComponents.toArray().every(step => step.isValid()) : false;
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

  private convertFileToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        const base64String = reader.result as string
        resolve(base64String);
      };
      reader.onerror = (error) => reject(error);
    });
  }
  
  
  async onPublish() {
    const recipe = await firstValueFrom(this.recipeService.post({
      title: this.recipeForm.controls['title'].value,
      description: this.recipeForm.controls['description'].value,
      category: this.recipeForm.controls['category'].value,
      difficulty: this.recipeForm.controls['difficulty'].value,
    }));

    for (const step of this.steps) {
      const createdStep = await firstValueFrom(this.stepService.post(recipe.id, {
        description: step.description,
        duration: step.duration,
        action: step.action,
        stepIndex: step.stepIndex
      }));

      for (const ingredient of step.ingredients) {
        await firstValueFrom(this.recipeIngredientService.post(recipe.id, createdStep.id, {
          quantity: ingredient.quantity,
          measurementUnit: ingredient.measurementUnit,
          ingredientId: ingredient.ingredient.id,
        }));
      }
    }

    const post = await firstValueFrom(this.postService.post({
      postPicture: await this.convertFileToBase64( this.recipeForm.controls['image'].value),
    }));

    await firstValueFrom(this.postService.put(post.id, { recipeId: recipe.id }));

    this.router.navigate(['/']);
  }
}
