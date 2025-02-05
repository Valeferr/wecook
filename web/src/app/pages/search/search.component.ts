import { ValueSetsService } from './../../services/model/value-sets.service';
import { ToastService } from './../../services/toast.service';
import { UserService } from './../../services/model/user.service';
import { Component, inject } from '@angular/core';
import { ItemSearchComponent } from "../../item-search/item-search.component";
import { MainFrameComponent } from "../main-frame/main-frame.component";
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors } from '@angular/forms';
import { AsyncPipe, CommonModule } from '@angular/common';
import { PostService } from '../../services/model/post.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { firstValueFrom, map, Observable, startWith } from 'rxjs';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [
    MainFrameComponent, 
    ItemSearchComponent, 
    FormsModule,
    MainFrameComponent,
    MatAutocompleteModule,
    MatFormFieldModule, 
    MatInputModule,
    AsyncPipe,
    CommonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent {
  protected readonly postService = inject(PostService);
  protected readonly userService = inject(UserService);
  protected readonly toastService = inject(ToastService);
  protected readonly valueSetsService = inject(ValueSetsService);

  protected filteredFoodCategories!: Observable<Array<string>>;
  protected foodOptions: Array<string> = new Array<string>;
  protected searchForm: FormGroup;

  searchType: string = 'users';
  searchQuery: string = '';
  results: any[] = [];

  constructor() {
    this.searchForm = new FormGroup({
      foodPreference: new FormControl<string | null>(null, [
        (control: AbstractControl): ValidationErrors | null => this.foodOptions.includes(control.value) ? null : { valueNotInList: true }
      ]),
    });
  }

  selectSearchType(type: string): void {
    this.searchType = type;

    this.results = [];
  }

  async ngOnInit() {
    this.foodOptions = await firstValueFrom(this.valueSetsService.foodCategories);

    this.filteredFoodCategories = this.searchForm.controls['foodPreference'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.foodOptions.filter(o => o.toLowerCase().includes(filterValue));
      }),
    );
  }
  

  search(): void {
    if (!this.searchQuery.trim()) return;
    
    if (this.searchQuery.trim() === '') return; 

    console.log("username da cercare", this.searchQuery);
    

    if (this.searchType === 'users') {
      this.userService.searchByUsername(this.searchQuery).subscribe(
        (response) => {
          if(response != null) {
            this.results = response;
          } else {
            this.toastService.showToast("No match", "WARNING");
          }
        },
        (error) => {
          console.error('Errore nella ricerca utenti:', error)
          this.toastService.showToast("Errore riprova", "ERROR");
        }
      );
    } else if (this.searchType === 'ingridients') {
      this.postService.searchByCategory(this.searchQuery).subscribe(
        (response) => {
          if(response != null) {
            this.results = response;
          } else {
            this.toastService.showToast("No match", "WARNING");
          }
        },
        (error) => {
          console.error('Errore nella ricerca utenti:', error)
          this.toastService.showToast("Errore riprova", "ERROR");
        }
      );
    } 
    // else if (this.searchType === 'recipes') {
    //   this.postService.searchRecipes(this.searchQuery).subscribe({
    //     next: (searchResults) => this.results = searchResults,
    //     error: (err) => console.error('Errore nella ricerca ricette:', err)
    //   });
    // }
  }
}
