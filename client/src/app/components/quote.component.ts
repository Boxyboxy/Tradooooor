import { IEXquote } from './../models';
import { ApiService } from './../api.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-quote',
  templateUrl: './quote.component.html',
  styleUrls: ['./quote.component.css'],
})
export class QuoteComponent implements OnInit {
  form!: FormGroup;
  stock: IEXquote | undefined;

  constructor(private apiSvc: ApiService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      ticker: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
      ]),
    });
  }

  async quote() {
    console.log(this.form.get('ticker')?.value);
    await this.apiSvc.getQuote(this.form.get('ticker')?.value).then((v) => {
      this.stock = v;
    });
    console.log(this.stock);

    console.log('quote clicked');
  }
}
