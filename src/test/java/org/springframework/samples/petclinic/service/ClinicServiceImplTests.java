package org.springframework.samples.petclinic.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for the service implementation
 *
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("ClassWithoutLogger")
public class ClinicServiceImplTests {

    private ClinicServiceImpl sut;
    
    @Mock 
    private OwnerRepository ownerRepository;
    @Mock 
    private PetRepository petRepository;
    @Mock 
    private VetRepository vetRepository;
    @Mock 
    private VisitRepository visitRepository;

    @Before
    public void setUp() throws Exception {
        sut = new ClinicServiceImpl(petRepository, vetRepository, ownerRepository, visitRepository);
    }

    /**
     * Pet owner can be found using its ID
     */
    @Test
    public void findPetOwnerById() {
        // given
        final int id = 123;
        Mockito.doReturn(createOwner()).when(ownerRepository).findById(id);
        // when
        final Owner res = sut.findOwnerById(id);
        // then
        assertThat(res).isNotNull();
    }

    /**
     * Save a new pet
     */
    @Test
    public void savePet() {
        // given
        final Pet pet = createPet();
        // when
        sut.savePet(pet);
        // then
        Mockito.verify(petRepository, Mockito.times(1)).save(pet);
    }

    /**
     * Pet adoption with a real pet
     */
    @Test
    public void petAdoptionWithRealPet() {
        // given
        final Owner owner = createOwner();
        final Pet pet = createPet();
        final ArgumentCaptor<Owner> ownerCaptor = ArgumentCaptor.forClass(Owner.class);
        // when
        sut.adoptAPet(owner, pet);
        // then
        Mockito.verify(ownerRepository).save(ownerCaptor.capture());
        assertThat(ownerCaptor.getValue().getPets()).hasSize(1).contains(pet);
    }

    /**
     * Pet adoption do nothing if pet is null
     */
    @Test
    public void petAdoptionWithNullPet() {
        // given
        final Pet pet = null;
        final Owner owner = createOwner();
        // when
        sut.adoptAPet(owner, pet);
        // then
        //Mockito.verify(ownerRepository, Mockito.never()).save(Mockito.any(Owner.class));
        // or
        Mockito.verifyZeroInteractions(ownerRepository);
    }

    private static Owner createOwner() {
        return new Owner();
    }

    private static Pet createPet() {
        Pet pet = new Pet();
        pet.setName("My little Pet");
        return pet;
    }
}
