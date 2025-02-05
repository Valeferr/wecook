import { Component, inject, input, OnInit } from '@angular/core';
import { Recipe } from '../../model/Recipe.model';
import { RecipeService } from '../../services/model/recipe.service';
import { firstValueFrom } from 'rxjs';
import { MainFrameComponent } from "../main-frame/main-frame.component";
import { PostService } from '../../services/model/post.service';
import { Post } from '../../model/Post.model';
import { RecipeIngredient } from '../../model/RecipeIngredient.model';
import { ActivatedRoute, Router } from '@angular/router';
import { StepComponent } from "./step/step.component";

@Component({
  selector: 'app-recipe',
  standalone: true,
  imports: [MainFrameComponent, StepComponent],
  templateUrl: './recipe.component.html',
  styleUrl: './recipe.component.css'
})
export class RecipeComponent implements OnInit {
  private readonly postService = inject(PostService);
  private readonly recipeService = inject(RecipeService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  protected ingredients: Array<RecipeIngredient> = new Array<RecipeIngredient>();
  protected post!: Post | null;
  protected recipe!: Recipe | null;
  
  async ngOnInit() {
    const postId = Number(this.route.snapshot.paramMap.get('postId'));

    this.post = await firstValueFrom(this.postService.get(postId));
    if (!this.post) {
      this.router.navigate(['/error'], { state: { statusCode: '404' } });
    }

    this.recipe = await firstValueFrom(this.recipeService.get(this.post.recipe.id));
    this.recipe.steps = this.recipe.steps.sort((a, b) => Number(a.stepIndex) - Number(b.stepIndex));
    
    const ingredients = new Map<number, RecipeIngredient>();
    this.recipe.steps.forEach(s => {
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
}
