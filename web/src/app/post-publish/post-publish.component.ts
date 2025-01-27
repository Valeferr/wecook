import { Component, ComponentRef, ViewChild, ViewContainerRef, inject } from '@angular/core';
import { StepComponent } from "../step/step.component";
import { ToastService } from '../services/toast.service';

@Component({
  selector: 'app-post-publish',
  standalone: true,
  imports: [],
  templateUrl: './post-publish.component.html',
  styleUrl: './post-publish.component.css'
})
//TODO: Aggiungere il drag and drop per i step
export class PostPublishComponent {
  @ViewChild('stepContainer', { read: ViewContainerRef }) stepContainer!: ViewContainerRef;

  private toast = inject(ToastService);
  stepRefs: ComponentRef<StepComponent>[] = [];

  ngAfterViewInit(): void {
    this.addStaticStep();
  }

  //TODO: inserire la ricetta nel database
  public onPublish() {
   if(this.stepRefs.length === 0) {
    this.toast.showToast('Aggiungere almeno un passaggio!', 'ERROR');
   } else {
    this.toast.showToast('Pubblicazione ricetta', 'SUCCESS');
   }
  }

  public addStep() {
    const stepNumber = this.stepRefs.length + 1;
    const componentRef = this.stepContainer.createComponent(StepComponent);
    componentRef.instance.title = `Step ${stepNumber}`;
    componentRef.instance.count = stepNumber;
    componentRef.instance.onDelete.subscribe(() => {
      this.deleteStep(componentRef);
    });
    this.stepRefs.push(componentRef); 
  }

  private deleteStep(componentRef: ComponentRef<StepComponent>) {
    const index = this.stepRefs.indexOf(componentRef);
    if (index !== -1) {
      this.stepRefs.splice(index, 1); 
    }
    componentRef.destroy(); 
    this.updateStepNumbers();
  }

  private addStaticStep() {
    const componentRef = this.stepContainer.createComponent(StepComponent);
    componentRef.instance.onDelete.subscribe(() => {
      this.deleteStep(componentRef);
    });
    this.stepRefs.push(componentRef); 
  }

  private updateStepNumbers() {
    this.stepRefs.forEach((ref, index) => {
      const stepNumber = index + 1; 
      ref.instance.title = `Step ${stepNumber}`;
      ref.instance.count = stepNumber;
    });
  }
}
