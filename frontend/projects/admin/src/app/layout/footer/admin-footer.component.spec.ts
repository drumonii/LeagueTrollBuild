import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { AdminFooterModule } from './admin-footer.module';
import { AdminFooterComponent } from './admin-footer.component';

describe('AdminFooterComponent', () => {
  let component: AdminFooterComponent;
  let fixture: ComponentFixture<AdminFooterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFooterModule]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should show footer', () => {
    const appVersion = fixture.debugElement.query(By.css('#app-version'));
    expect(appVersion.nativeElement.textContent.trim()).toContain('Version:');

    const homeLink = fixture.debugElement.query(By.css('#troll-build-home-link'));
    expect(homeLink.nativeElement.textContent.trim()).toBe('Troll Build Home');
    expect(homeLink.nativeElement.href).toBe('https://www.loltrollbuild.com/');
  });
});
