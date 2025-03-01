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
  protected readonly route: ActivatedRoute = inject(ActivatedRoute);
  private readonly postService = inject(PostService);
  protected readonly userService: UserService = inject(UserService);
  protected readonly valueSetsService = inject(ValueSetsService);
  protected readonly auth: AuthService = inject(AuthService);

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
        Validators.minLength(6)
      ]),
      favoriteDish: new FormControl<string | null>(null, [
      ]),
      foodPreference: new FormControl<string | null>(null, [
        (control: AbstractControl): ValidationErrors | null => this.foodOptions.includes(control.value) ? null : { valueNotInList: true }
      ]),
      picture: new FormControl<File | null>(null)
    });
  }

  async ngOnInit() {
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
  
  public followUser() {
    this.userService.followUser({
      followedId: this.user!.id
    }).subscribe((response) => {
      this.user!.followers++;
      this.user!.isFollowing = true
    });
  }

  public unfollowUser() {
    this.userService.unfollowUser(this.user!.id).subscribe((response) => {
      this.user!.followers--;
      this.user!.isFollowing = false
    });
  }

  enableEdit() {
    this.isEditing = true;
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

  async onSaveProfileChanges() {
    const editPayload: Partial<StandardUser> = {};
    
    if (this.editForm.get('username')?.value) {
      editPayload.username = this.editForm.get('username')?.value;
    }

    if (this.editForm.get('favoriteDish')?.value) {
      editPayload.favoriteDish = this.editForm.get('favoriteDish')?.value;
    } else if (this.editForm.get('favoriteDish')?.touched && this.editForm.get('favoriteDish')?.value !== this.user?.favoriteDish) {
      editPayload.favoriteDish = "";
    }

    if (this.editForm.get('foodPreference')?.value) {
      editPayload.foodPreference = this.editForm.get('foodPreference')?.value;
    } else if (this.editForm.get('foodPreference')?.touched && this.editForm.get('foodPreference')?.value !== this.user?.foodPreference) {
      editPayload.foodPreference = "";
    }

    if (this.editForm.get('picture')?.value) {
      editPayload.picture = await this.convertFileToBase64( this.editForm.controls['picture'].value)
    }

    if (Object.keys(editPayload).length === 0) {
      return;
    }

    editPayload.id = this.user!.id;
    
    this.userService.patch(editPayload).subscribe((response) => {
      this.isEditing = false;
      this.user = {
        ...this.user!,
        ...editPayload
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    
    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      this.editForm.patchValue({ picture: file });
  
      const reader = new FileReader();
      reader.onload = () => {
        this.imageSrc = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  cancelEdit() {
    this.isEditing = false;
    this.editForm.reset();
  }
}
