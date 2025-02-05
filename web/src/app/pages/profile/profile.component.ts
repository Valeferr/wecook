import { ActivatedRoute } from '@angular/router';
import { Component, inject } from '@angular/core';
import { Roles, User } from '../../model/User.model';
import { StandardUser } from '../../model/StandardUser.model';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { ToastService } from '../../services/toast.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ModeratorUser } from '../../model/ModeratorUser.model';
import { MainFrameComponent } from "../main-frame/main-frame.component";
import { AsyncPipe } from '@angular/common';
import { firstValueFrom, map, Observable, startWith } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ValueSetsService } from '../../services/model/value-sets.service';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../services/model/user.service';

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
export class ProfileComponent {
  protected readonly valueSetsService = inject(ValueSetsService);
  protected readonly toast = inject(ToastService);
  protected readonly auth: AuthService = inject(AuthService);
  protected readonly userService: UserService = inject(UserService);
  protected readonly route: ActivatedRoute = inject(ActivatedRoute);
  
  protected filteredFoodCategories!: Observable<Array<string>>;
  protected foodOptions: Array<string> = new Array<string>;
  protected editForm: FormGroup;
  
  standardUser: StandardUser | null = null;
  moderatorUser: ModeratorUser | null = null;
  user: User | null = this.auth.getUser();
  Roles = Roles;
  
  isEditing = false;
  imageSrc: string = 'assets/default_recipe.png';  
  

  constructor(private http: HttpClient) {
    this.initializeUserData();
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

  //TODO: follow
  public addFollow() {

  }

  async ngOnInit() {
      this.foodOptions = await firstValueFrom(this.valueSetsService.foodCategories);
  
      this.filteredFoodCategories = this.editForm.controls['foodPreference'].valueChanges.pipe(
        startWith(''),
        map((value) => {
          const filterValue = (value || '').toLowerCase();
          return this.foodOptions.filter(o => o.toLowerCase().includes(filterValue));
        }),
      );
    }

  private initializeUserData() {
    if (this.user) {
      if (this.user.role === Roles.Standard) {
        this.standardUser = this.user as StandardUser;
      } else if (this.user.role === Roles.Moderator) {
        this.moderatorUser = this.user as ModeratorUser;
      }
    }
  }

  toggleEdit() {
    this.isEditing = true;
  }

  saveChanges() {
    if (!this.standardUser) return;    

    const updatedUser: StandardUser = {
      ...this.standardUser,
      username: this.editForm.get('username')?.value || this.standardUser.username,
      favoriteDish: this.editForm.get('favoriteDish')?.value || this.standardUser.favoriteDish,
      foodPreference: this.editForm.get('foodPreference')?.value || this.standardUser.foodPreference,
    };

    console.log("Dati da aggiornare", JSON.stringify(updatedUser));

    this.userService.updateUser(updatedUser).subscribe(
      (response) => this.handleUpdateSuccess(response),
      (error) => this.handleUpdateError(error)
    );
  }

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

  private handleUpdateSuccess(response: StandardUser) {
    console.log('Dati aggiornati con successo:', response);
    this.isEditing = false;
    this.standardUser = response;
    this.toast.showToast('Data updated successfully.', 'SUCCESS');
  }

  private handleUpdateError(error: any) {
    if (error.status === 409) {
      console.error('Errore 409: Conflitto nei dati. L\'username potrebbe essere già in uso.');
      this.toast.showToast('Username already in use. Insert another one.', 'WARNING');
    } else {
      console.error('Errore durante l\'aggiornamento dei dati:', error);
      this.toast.showToast('An error occurred while saving data. Please try again.', 'ERROR');
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

  // TODO: Funzione per visualizzare il pop-up (Follower)
  followersClicked() {
    
  }
}
