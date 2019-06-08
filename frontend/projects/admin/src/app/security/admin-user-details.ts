export interface AdminUserDetails {
  username: string;
  authorities: GrantedAuthority[];
}

interface GrantedAuthority {
  authority: string;
}
