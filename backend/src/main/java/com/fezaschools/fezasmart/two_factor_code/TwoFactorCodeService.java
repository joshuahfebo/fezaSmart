package com.fezaschools.fezasmart.two_factor_code;

import com.fezaschools.fezasmart.events.BeforeDeleteUser;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TwoFactorCodeService {

    private final TwoFactorCodeRepository twoFactorCodeRepository;
    private final UserRepository userRepository;

    public TwoFactorCodeService(final TwoFactorCodeRepository twoFactorCodeRepository,
            final UserRepository userRepository) {
        this.twoFactorCodeRepository = twoFactorCodeRepository;
        this.userRepository = userRepository;
    }

    public List<TwoFactorCodeDTO> findAll() {
        final List<TwoFactorCode> twoFactorCodes = twoFactorCodeRepository.findAll(Sort.by("id"));
        return twoFactorCodes.stream()
                .map(twoFactorCode -> mapToDTO(twoFactorCode, new TwoFactorCodeDTO()))
                .toList();
    }

    public TwoFactorCodeDTO get(final Integer id) {
        return twoFactorCodeRepository.findById(id)
                .map(twoFactorCode -> mapToDTO(twoFactorCode, new TwoFactorCodeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TwoFactorCodeDTO twoFactorCodeDTO) {
        final TwoFactorCode twoFactorCode = new TwoFactorCode();
        mapToEntity(twoFactorCodeDTO, twoFactorCode);
        return twoFactorCodeRepository.save(twoFactorCode).getId();
    }

    public void update(final Integer id, final TwoFactorCodeDTO twoFactorCodeDTO) {
        final TwoFactorCode twoFactorCode = twoFactorCodeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(twoFactorCodeDTO, twoFactorCode);
        twoFactorCodeRepository.save(twoFactorCode);
    }

    public void delete(final Integer id) {
        final TwoFactorCode twoFactorCode = twoFactorCodeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        twoFactorCodeRepository.delete(twoFactorCode);
    }

    private TwoFactorCodeDTO mapToDTO(final TwoFactorCode twoFactorCode,
            final TwoFactorCodeDTO twoFactorCodeDTO) {
        twoFactorCodeDTO.setId(twoFactorCode.getId());
        twoFactorCodeDTO.setCode(twoFactorCode.getCode());
        twoFactorCodeDTO.setExpiresAt(twoFactorCode.getExpiresAt());
        twoFactorCodeDTO.setUsed(twoFactorCode.getUsed());
        twoFactorCodeDTO.setCreatedAt(twoFactorCode.getCreatedAt());
        twoFactorCodeDTO.setUser(twoFactorCode.getUser() == null ? null : twoFactorCode.getUser().getId());
        return twoFactorCodeDTO;
    }

    private TwoFactorCode mapToEntity(final TwoFactorCodeDTO twoFactorCodeDTO,
            final TwoFactorCode twoFactorCode) {
        twoFactorCode.setCode(twoFactorCodeDTO.getCode());
        twoFactorCode.setExpiresAt(twoFactorCodeDTO.getExpiresAt());
        twoFactorCode.setUsed(twoFactorCodeDTO.getUsed());
        twoFactorCode.setCreatedAt(twoFactorCodeDTO.getCreatedAt());
        final User user = twoFactorCodeDTO.getUser() == null ? null : userRepository.findById(twoFactorCodeDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        twoFactorCode.setUser(user);
        return twoFactorCode;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final TwoFactorCode userTwoFactorCode = twoFactorCodeRepository.findFirstByUserId(event.getId());
        if (userTwoFactorCode != null) {
            referencedException.setKey("user.twoFactorCode.user.referenced");
            referencedException.addParam(userTwoFactorCode.getId());
            throw referencedException;
        }
    }

}
