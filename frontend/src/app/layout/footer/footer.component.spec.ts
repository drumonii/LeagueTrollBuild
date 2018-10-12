import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { FooterComponent } from './footer.component';
import { Version } from '@model/version';
import { VersionsService } from '@service/versions.service';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FooterComponent],
      providers: [VersionsService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
  });

  it('should show latest saved Version in footer', inject([VersionsService], (versionsService: VersionsService) => {
    const latestVersion = new Version('8.2.1');

    spyOn(versionsService, 'getLatestVersion').and.returnValue(of(latestVersion));

    fixture.detectChanges();

    const latestSavedVersion = fixture.debugElement.query(By.css('#latest-saved-version'));
    expect(latestSavedVersion.nativeElement.textContent.trim()).toBe(`Patch ${latestVersion.patch}`);

    const about = fixture.debugElement.query(By.css('#about'));
    expect(about).toBeTruthy();

    const githubLink = fixture.debugElement.query(By.css('#github-link'));
    expect(githubLink.nativeElement.textContent.trim()).toBe('GitHub');
    expect(githubLink.nativeElement.href).toBe('https://github.com/drumonii/LeagueTrollBuild');

    const disclaimer = fixture.debugElement.query(By.css('#disclaimer'));
    expect(disclaimer).toBeTruthy();
  }));
});
