import { ActivatedRoute, Router } from '@angular/router';
import { Component, inject, OnInit } from '@angular/core';
import { Roles, User } from '../../model/User.model';
import { StandardUser } from '../../model/StandardUser.model';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ModeratorUser } from '../../model/ModeratorUser.model';
import { MainFrameComponent } from "../main-frame/main-frame.component";
import { AsyncPipe } from '@angular/common';
import { firstValueFrom, map, Observable, startWith } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ValueSetsService } from '../../services/model/value-sets.service';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../services/model/user.service';
import { Post } from '../../model/Post.model';
import { PostService } from '../../services/model/post.service';

//TODO: inserire le allergie
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    FormsModule,
    MainFrameComponent,
    MatFormFieldModule, 
    MatAutocompleteModule,
    MatInputModule,
    AsyncPipe,
    ReactiveFormsModule,
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  protected readonly router = inject(Router);
  private readonly postService = inject(PostService);
  protected readonly userService: UserService = inject(UserService);

  protected readonly valueSetsService = inject(ValueSetsService);
  protected readonly auth: AuthService = inject(AuthService);
  protected readonly route: ActivatedRoute = inject(ActivatedRoute);
  
  protected filteredFoodCategories!: Observable<Array<string>>;
  protected foodOptions: Array<string> = new Array<string>;
  protected editForm: FormGroup;
  
  protected user: StandardUser | null = null;
  protected posts: Array<Post> = new Array<Post>();
  Roles = Roles;
  
  isEditing = false;
  imageSrc: string = 'assets/blank_user.png';  
  
  constructor(private http: HttpClient) {
    this.editForm = new FormGroup({
      username: new FormControl<string | null>(null, [
      ]),
      favoriteDish: new FormControl<string | null>(null, [
      ]),
      foodPreference: new FormControl<string | null>(null, [
        (control: AbstractControl): ValidationErrors | null => this.foodOptions.includes(control.value) ? null : { valueNotInList: true }
      ]),
    });
  }

  async ngOnInit() {
    debugger
    const userId = Number(this.route.snapshot.paramMap.get('userId'));
    
    this.user = await firstValueFrom(this.userService.get<StandardUser>(userId));
    if (!this.user) {
      this.router.navigate(['/error'], { state: { statusCode: '404' } });
    }
    
    this.posts = await firstValueFrom(this.postService.getUserPosts(this.user.id));

    this.foodOptions = await firstValueFrom(this.valueSetsService.foodCategories);
  
    this.filteredFoodCategories = this.editForm.controls['foodPreference'].valueChanges.pipe(
      startWith(''),
      map((value) => {
        const filterValue = (value || '').toLowerCase();
        return this.foodOptions.filter(o => o.toLowerCase().includes(filterValue));
      }),
    );
  }
  
  // public addFollow() {
  //   this.userService.followUser(this.user!.id).subscribe(
  //     (response) => this.handleUpdateSuccess(response),
  //     (error) => this.handleUpdateError(error)
  //   );
  // }

  toggleEdit() {
    this.isEditing = true;
  }

  // saveChanges() {
  //   if (!this.standardUser) return;    

  //   const updatedUser: StandardUser = {
  //     ...this.standardUser,
  //     username: this.editForm.get('username')?.value || this.standardUser.username,
  //     favoriteDish: this.editForm.get('favoriteDish')?.value || this.standardUser.favoriteDish,
  //     foodPreference: this.editForm.get('foodPreference')?.value || this.standardUser.foodPreference,
  //   };

  //   console.log("Dati da aggiornare", JSON.stringify(updatedUser));

  //   this.userService.patch(updatedUser).subscribe(
  //     (response) => {
  //       this.standardUser = response;
  //       this.isEditing = false;
  //     },
  //     (error) => {
  //       this.handleUpdateError(error)
  //     }
  //   );
  // }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
  
      const reader = new FileReader();
      reader.onload = () => {
        this.imageSrc = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  private handleUpdateError(error: any) {
    if (error.status === 409) {
      console.error('Errore 409: Conflitto nei dati. L\'username potrebbe essere già in uso.');
    } else {
      console.error('Errore durante l\'aggiornamento dei dati:', error);
    }
    this.cancelEdit();
  }

  cancelEdit() {
    this.isEditing = false;
  }

  // TODO: Funzione da implementare per il grafico (Moderatore)
  initializeChart(): void {
    const ctx = document.getElementById('reportChart') as HTMLCanvasElement;
  }
}
