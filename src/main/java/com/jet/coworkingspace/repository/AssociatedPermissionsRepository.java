package com.jet.coworkingspace.repository;

import com.jet.coworkingspace.model.AssociatedPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AssociatedPermissionsRepository extends JpaRepository<AssociatedPermissions,Long> {

    @Query(value="SELECT associatedpermissions.id FROM  associatedpermissions where associatedpermissions.user_id = :userId and associatedpermissions.permission = :permission", nativeQuery = true)
    Long findPermissionByUID(@Param("userId") Long userId, @Param("permission") String permission);
}

