import { ValueSetsService } from './../../services/model/value-sets.service';
import { UserService } from './../../services/model/user.service';
import { Component, inject } from '@angular/core';
import { MainFrameComponent } from "../main-frame/main-frame.component";
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors } from '@angular/forms';
import { AsyncPipe, CommonModule } from '@angular/common';
import { PostService } from '../../services/model/post.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { firstValueFrom, map, Observable, startWith } from 'rxjs';
import { StandardUser } from '../../model/StandardUser.model';
import { Post } from '../../model/Post.model';
import { IngredientService } from '../../services/model/ingredient.service';
import { SearchResult, SearchResultComponent } from "./search-result/search-result.component";

export enum SearchTypes {
  USERNAME = 0,
  RECIPE_NAME = 1,
  RECIPE_CATEGORY = 2,
  RECIPE_INGREDIENT = 3,
}

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [
    MainFrameComponent,
    FormsModule,
    MainFrameComponent,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    AsyncPipe,
    CommonModule,
    ReactiveFormsModule,
    SearchResultComponent
],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})

export class SearchComponent {
  protected readonly valueSetsService = inject(ValueSetsService);
  protected readonly ingredientService = inject(IngredientService);
  protected readonly postService = inject(PostService);
  protected readonly userService = inject(UserService);

  protected searchForm: FormGroup;
  protected filteredRecipeCategories!: Observable<Array<string>>;
  protected recipeCategories: Array<string> = new Array<string>;
  protected filteredRecipeIngredients!: Observable<Array<string>>;
  protected recipeIngredients: Array<string> = new Array<string>;

  SearchTypes = SearchTypes;
  searchType: SearchTypes = SearchTypes.USERNAME;

  protected isSearching: boolean = false;
  protected results: Array<SearchResult> = new Array<SearchResult>();

  constructor() {
    this.searchForm = new FormGroup({
      username: new FormControl<string | null>(null),
      recipeName: new FormControl<string | null>(null),
      recipeCategory: new FormControl<string | null>(null, [
        (control: AbstractControl): ValidationErrors | null => this.recipeCategories.includes(control.value) ? null : { valueNotInList: true }
      ]),
      recipeIngredient: new FormControl<string | null>(null, [
        (control: AbstractControl): ValidationErrors | null => this.recipeIngredients.includes(control.value) ? null : { valueNotInList: true }
      ]),
    });
  }

  async ngOnInit() {
    this.recipeCategories = await firstValueFrom(this.valueSetsService.foodCategories);
    this.filteredRecipeCategories = this.searchForm.controls['recipeCategory'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.recipeCategories.filter(c => c.toLowerCase().includes(filterValue));
      }),
    );

    this.recipeIngredients = (await firstValueFrom(this.ingredientService.getAll())).map(i => i.name);
    this.filteredRecipeIngredients = this.searchForm.controls['recipeIngredient'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.recipeIngredients.filter(i => i.toLowerCase().includes(filterValue));
      }),
    );
  }

  onSelectSearchType(searchType: SearchTypes): void {
    this.results = new Array<SearchResult>();
    this.searchType = searchType;
    this.isSearching = false;
    this.searchForm.reset();
  }
  
  search(): void {
    this.isSearching = true;
    
    switch (this.searchType) {
      case SearchTypes.USERNAME:
        this.userService.searchByUsername(this.searchForm.controls['username'].value).subscribe(
          (user) => {
            this.results = user.map(u => ({
              picture: u.picture ?? 'assets/blank_user.png',
              title: u.username,
              url: `/profile/${u.id}`,
              id: u.id,
            }));
          }
        )
        break;
      case SearchTypes.RECIPE_NAME:
        this.postService.searchByTitle(this.searchForm.controls['recipeName'].value).subscribe(
          (posts) => {
            this.results = posts.map(p => ({
              picture: p.picture,
              title: p.recipe.title,
              url: `/recipe/${p.id}`,
              id: p.id,
            }));
          }
        )
        break;
      case SearchTypes.RECIPE_CATEGORY:
        this.postService.searchByCategory(this.searchForm.controls['recipeCategory'].value).subscribe(
          (posts) => {
            this.results = posts.map(p => ({
              picture: p.picture,
              title: p.recipe.title,
              url: `/recipe/${p.id}`,
              id: p.id,
            }));
          }
        )
        break;
      case SearchTypes.RECIPE_INGREDIENT:
        this.postService.searchByIngredient(this.searchForm.controls['recipeIngredient'].value).subscribe(
          (posts) => {
            this.results = posts.map(p => ({
              picture: p.picture,
              title: p.recipe.title,
              url: `/recipe/${p.id}`,
              id: p.id,
            }));
          }
        )
        break;
    }
  }
}
