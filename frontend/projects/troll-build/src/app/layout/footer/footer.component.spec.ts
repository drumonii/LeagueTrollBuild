import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { NbEvaIconsModule } from '@nebular/eva-icons';

import { of } from 'rxjs';

import { FooterModule } from './footer.module';
import { FooterComponent } from './footer.component';
import { Version } from '@ltb-model/version';
import { VersionsService } from './versions.service';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NbEvaIconsModule, FooterModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
  });

  it('should show latest saved Version in footer', inject([VersionsService], (versionsService: VersionsService) => {
    const latestVersion: Version = {
      patch: '8.2.1',
      major: 8,
      minor: 2,
      revision: 1
    };

    spyOn(versionsService, 'getLatestVersion').and.returnValue(of(latestVersion));

    fixture.detectChanges();

    const about = fixture.debugElement.query(By.css('[data-e2e="about"]'));
    expect(about).toBeTruthy();

    const githubLink = fixture.debugElement.query(By.css('#github-link'));
    expect(githubLink.nativeElement.textContent.trim()).toBe('GitHub');
    expect(githubLink.nativeElement.href).toBe('https://github.com/drumonii/LeagueTrollBuild');

    const disclaimer = fixture.debugElement.query(By.css('[data-e2e="disclaimer"]'));
    expect(disclaimer).toBeTruthy();

    const latestSavedVersion = fixture.debugElement.query(By.css('[data-e2e="latest-saved-version"]'));
    expect(latestSavedVersion.nativeElement.textContent.trim()).toBe(`Patch ${latestVersion.patch}`);
  }));
});
