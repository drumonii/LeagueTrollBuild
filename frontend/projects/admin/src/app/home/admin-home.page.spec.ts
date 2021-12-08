import { ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';

import { AdminHomeModule } from './admin-home.module';
import { AdminHomePage } from './admin-home.page';
import { AdminTitleService } from '@admin-service/admin-title.service';
import { AppHealthComponent } from './dashboard/app/app-health.component';
import { EnvComponent } from './dashboard/env/env.component';
import { ResourcesComponent } from './dashboard/resources/resources.component';
import { HttpStatsComponent } from './dashboard/http/http-stats.component';

describe('AdminHomePage', () => {
  let component: AdminHomePage;
  let fixture: ComponentFixture<AdminHomePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NoopAnimationsModule, AdminHomeModule]
    })
    .compileComponents();
  });

  beforeEach(inject([AdminTitleService], (title: AdminTitleService) => {
    fixture = TestBed.createComponent(AdminHomePage);
    component = fixture.componentInstance;

    spyOn(title, 'resetTitle').and.callThrough();

    fixture.detectChanges();
  }));

  afterEach(inject([AdminTitleService], (title: AdminTitleService) => {
    expect(title.resetTitle).toHaveBeenCalled();
  }));

  it('should create', () => {
    expect(fixture.debugElement.query(By.directive(AppHealthComponent))).toBeTruthy();
    expect(fixture.debugElement.query(By.directive(EnvComponent))).toBeTruthy();
    expect(fixture.debugElement.query(By.directive(ResourcesComponent))).toBeTruthy();
    expect(fixture.debugElement.query(By.directive(HttpStatsComponent))).toBeTruthy();
  });

});
